package com.vjmp.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.vjmp.InputHandler;
import com.vjmp.InputHandler.Buttons;
import com.vjmp.Map;
import com.vjmp.entities.Entity.EntityType;
import com.vjmp.entities.drawable.DrawableEntity;
import com.vjmp.entities.drawable.FinishLine;
import com.vjmp.entities.drawable.MessageBox;
import com.vjmp.entities.drawable.SpikeTrigger;
import com.vjmp.entities.drawable.TriggerEntity;
import com.vjmp.entities.drawable.TriggerEntity.TriggerType;
import com.vjmp.gfx.Camera;
import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.Sprite.Dir;
import com.vjmp.gfx.Sprite.SpriteType;
import com.vjmp.managers.GuiManager;

/**
 * A szerkesztõ felület, feladatai minden fajta szerkesztõi funkció. DrawableEntity-ket, {@link TriggerEntity}-ket
 * helyez el, illetve töröl a pályán.
 * 
 *
 */
public class Editor implements Serializable, ActionListener {
	
	
	
	private static final long serialVersionUID = 1L;
	private static final int BLOCK_SIZE = 15;
	private static int sprite_index = 0;
	private static EntityType entityType = EntityType.BLOCK;
	private static DrawableEntity player = null;
	private static InputHandler.Buttons mouse_button = Buttons.NONE;
	private static String MessageString = null;
	private static TriggerType triggerType=  null;
	private static SpriteType spriteType = null;

	public static TextureList textureList = new TextureList();
	
	private Map map = null;



	private boolean polling_for_mouse_release = true;
	private SelectRectangle selectRectangle = null;
	private GuiManager guiManager = null;

	private Point old_camera_pos = null;

	transient private InputHandler inputHandler = null;

	/**
	 * Konstruktor 
	 * @param inputHandler : {@link InputHandler} : az input kezelõ
	 * @param WIDTH : int - a szerkesztõ szélessége
	 * @param HEIGHT : int - a szerkesztõ magassága
	 */
	public Editor(InputHandler inputHandler, int WIDTH, int HEIGHT) {
		map = new Map(WIDTH, HEIGHT, true);
		this.inputHandler = inputHandler;

		selectRectangle = new SelectRectangle(this);
	}

	/**
	 * Beállitja az inputHandler-t
	 * @param inputHandler : {@link InputHandler}
	 */
	public void setInputHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	/**
	 * Beállitja a GuiManager-t.
	 * @param componentManager : {@link GuiManager}
	 */
	public void setComponentManager(GuiManager componentManager) {
		this.guiManager = componentManager;
	}

	/**
	 * Hozzáadja a megfelelõ entitást a pályához a rect {@link Rectangle} alapján.
	 * A current_sprite_index alapján meghatározza az entitás textúráját
	 * @param rect
	 */
	public void addSprite(Rectangle rect) {
		
		//Kezdetleges megoldás, spawn pointot a spooky.png képpel kiválasztva rakhatunk le
		//TODO:Refactor triggerré
		if (textureList.GetPath(sprite_index).equals("./res/png/spooky.png")) {
			if (player == null) {
				player = new DrawableEntity("./res/png/spooky.png", rect.x, rect.y,
						true);
			} else {
				player.setLocation(rect.x, rect.y);
			}
		} else {
			//Létrehozzuk a az entitást a spriteType,entityType,rectangle,textúra alapján
			DrawableEntity drawableEntity = GetDrawableEnityTypeFromSpriteIndex(rect);
			if (drawableEntity != null) {
				map.add(drawableEntity);
			} else {
				System.out.println("HIBA!");
			}
		}
	}
	/**
	 * 
	 * @param rect : {@link Rectangle} - a hozzáadandó entitás {@link Rectangle}-je
	 * @return ret : {@link DrawableEntity} - a megalkotott entitás.
	 */
	private DrawableEntity GetDrawableEnityTypeFromSpriteIndex(Rectangle rect) {

		DrawableEntity ret = null;
		switch (entityType) {
		case BLOCK:
			ret = new DrawableEntity(textureList.GetPath(sprite_index), rect,
					true, guiManager.getWalls(),spriteType);
			
			break;
		case TRIGGER:
			ret = GetTriggerEntityFromType(rect);
			break;
		}

		return ret;

	}

	/**
	 * Az editor a kapott adatok alapján megalkotja a megfelelõ {@link TriggerEntity}-t.
	 * 
	 * @param rect : {@link Rectangle} - a hozzáadandó entitás {@link Rectangle}-je
	 * @return ret : {@link DrawableEntity} - valójában egy TriggerEntity, amit a kapott adatok alapján
	 * alkotunk.
	 */
	private DrawableEntity GetTriggerEntityFromType(Rectangle rect) {
		TriggerEntity ret = null;
		switch (triggerType) {
		case SPIKE:
			ret = new SpikeTrigger(textureList.GetPath(sprite_index), rect,
					true, guiManager.getWalls(),spriteType);
			break;
		case FINISH_LINE:
			ret = new FinishLine(textureList.GetPath(sprite_index), rect, true);
			break;
		case MESSAGE_BOX:
			if (MessageString != null) {
				ret = new MessageBox(MessageString,
						textureList.GetPath(sprite_index), rect, true,
						guiManager.getWalls(),SpriteType.SCALE);
			}
			break;
		}
		return ret;
	}
	/**
	 * Frissiti az editort. Köztük a {@link Map}-t,a {@link SelectRectangle}-t.
	 * @param camera : {@link Camera}
	 */
	public void update(Camera camera) {
		map.update(camera);
		spriteTypeUpdate();
		BlockLogic(camera);
		if (guiManager != null) {
			guiManager.update();
		}
		selectRectangle.update(camera);
	}
	private void spriteTypeUpdate(){
		if(inputHandler.SHIFT.isPressed()) {
			spriteType = SpriteType.NORMAL;
			
		} else if(inputHandler.CTRL.isPressed()) {
			spriteType = SpriteType.REPEAT;
		} else {
			spriteType = SpriteType.SCALE;
		}
	}

	/**
	 * Az inputHandler alapján frissiti az alkotandó entitás {@link SpriteType}-ját,
	 * a mouse mozgása alapján pedig meghatározza a DrawableEntity {@link Rectangle}-jét.
	 * @param camera
	 */
	private void BlockLogic(Camera camera) {
		if (inputHandler.MOUSE.button.isPressed()) {
			polling_for_mouse_release = true;
			//kell egy régi kamera állás a helyes rectangle számitás miatt
			//ha mozgott közbe a kamera, szükség van rá
			if (old_camera_pos == null) {
				old_camera_pos = new Point(camera.pos_x, camera.pos_y);
			}
			if (inputHandler.MOUSE.button_clicked == Buttons.LEFT) {
				mouse_button = Buttons.LEFT;

			} else if (inputHandler.MOUSE.button_clicked == Buttons.RIGHT) {
				mouse_button = Buttons.RIGHT;
			}
		}
		if (polling_for_mouse_release) {
			if (!inputHandler.MOUSE.button.isPressed()) {
				polling_for_mouse_release = false;

				if (mouse_button == Buttons.LEFT) {
					
					//kiszámitjuk a megfelelõ rectangle-t a régi és új kamera állás alapján
					
					Rectangle rect = GenerateScaledRectangleFromMouse(camera);
					if(rect == null) {
						System.out.println("HIBA,rect == null!");
					} else {
						//minden oké, mehet az entitás
						addSprite(rect);
					}
				} else if (mouse_button == Buttons.RIGHT) {

					//törlést végezzük
					Rectangle rect = GenerateScaledRectangleFromMouse(camera);
					removeSprite(rect);
				}
				// mar nincs ra szukseg, mivel legeneraltuk a rectangle-t a
				// remove,illetve add-hoz
				old_camera_pos = null;
			}

		}

	}

	/**
	 * A {@link Rectangle} alapján törli az ezzel ütközõ entitásokat.
	 * @param rect : {@link Rectangle} - a mouse által meghatározott {@link Rectangle}.
	 */
	private void removeSprite(Rectangle rect) {
		map.remove(rect);

	}

	/**
	 * A mouse gomb lenyomása alatt meghatározott {@link Rectangle}-t generálja ki. 
	 * @param camera : {@link Camera}
	 * @return ret : {@link Rectangle} - a kigenerált {@link Rectangle}
	 */
	public Rectangle GenerateScaledRectangleFromMouse(Camera camera) {

		// camera positionje mindig mod15 == 0, mivel ennyi az editor speedje,
		// ezert
		// csak a mouse position-t kell div15 ölni
		
		Rectangle ret = null;
		
		Point old_pos = AddOldCameraPos(DivBlockSize(inputHandler.MOUSE
				.getOldPos()));
		Point pos = AddCameraPos(DivBlockSize(inputHandler.MOUSE.getPos()),
				camera);
		int width = Math.abs(old_pos.x - pos.x) + BLOCK_SIZE;
		int height = Math.abs(old_pos.y - pos.y) + BLOCK_SIZE;

		int x = Math.min(old_pos.x, pos.x);
		int y = Math.min(old_pos.y, pos.y);
		if(spriteType == SpriteType.REPEAT || spriteType == SpriteType.SCALE) {
			ret = new Rectangle(x, y, width, height);
		} else {
			ret = new Rectangle(pos.x,pos.y,width,height);
		}
		return ret;
	}

	/**
	 * Meghatározza az ezelõtti mouse esemény során keletkezett mouse poziciót.
	 * @param p : {@link Point} - a jelenlegi mouse pozició.
	 * @return
	 */
	private Point AddOldCameraPos(Point p) {
		p.x = p.x - old_camera_pos.x;
		p.y = p.y - old_camera_pos.y;
		return p;
	}

	/**
	 * Meghatározza a jelenlegi mouse esemény során keletkezett mouse poziciót.
	 * @param p
	 * @param c
	 * @return p : Point - a változtatott point
	 */
	public Point AddCameraPos(Point p, Camera c) {
		p.x = p.x - c.pos_x;
		p.y = p.y - c.pos_y;
		
		return p;
	}

	/**
	 * A BLOCK_SIZE alapján meghatározott konstanssal elosztja a p {@link Point}-ot.
	 * @param p : {@link Point}
	 * @return elosztott pont : {@link Point}
	 */
	public Point DivBlockSize(Point p) {
		return new Point(p.x - (p.x % BLOCK_SIZE), p.y - (p.y % BLOCK_SIZE));
	}

	/**
	 * Kirajzolja az editort.
	 * @param g : {@link Graphics}
	 */
	public void draw(Graphics g) {
		map.draw(g);
		if (player != null) {
			player.draw(g);
		}
		selectRectangle.draw(g);
		drawSpriteBorderLines(g);
	}

	/**
	 * Kirajzolja a sprite-ok oldalait, az sprite falainak(fal -> ahol ütközés áll fenn) megfelelõen
	 * @param g : {@link Graphics}
	 */
	private void drawSpriteBorderLines(Graphics g) {
		for(DrawableEntity sprite : map){
			Rectangle rect = sprite.getRect();
			Color old_tmp = g.getColor();
			
			g.setColor(getColorFromWall(sprite,Dir.NORTH));
			g.drawLine(rect.x,rect.y,rect.x+rect.width,rect.y);
			
			g.setColor(getColorFromWall(sprite, Dir.SOUTH));
			g.drawLine(rect.x, rect.y+rect.height,rect.x+rect.width,rect.y+rect.height);
			
			g.setColor(getColorFromWall(sprite,Dir.WEST));
			g.drawLine(rect.x, rect.y, rect.x, rect.y+rect.height);
			
			g.setColor(getColorFromWall(sprite, Dir.EAST));
			g.drawLine(rect.x+rect.width, rect.y,rect.x+rect.width,rect.y+rect.height);
			
			g.setColor(old_tmp);
		}
		
	}
	
	/**
	 * Visszadja a border szinét a sprite direction irányú falának megfelelõen
	 * @param sprite : {@link Sprite} - a vizsgált sprite
	 * @param dir : {@link Dir} - a vizsgált direction
	 * @return {@link Color}
	 */
	private Color getColorFromWall(DrawableEntity sprite,Dir dir){
		if(sprite.getWall(dir)) {
			return Color.red;
		} else {
			return Color.green;
		}
		
	}
	
	/**
	 * Serializáció
	 * @param stream : {@link ObjectOutputStream}
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {
		if (player == null) {
			System.out.println("Nincs spawnpoint");
			return;
		}
		System.out.println("serialization");
		stream.writeUTF("./res/png/spooky.png");
		stream.writeInt(player.GetPosX());
		stream.writeInt(player.GetPosY());
		stream.writeObject(map);

	}

	/**
	 * Deserializáció
	 * @param in : {@link ObjectInputStream}
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {

		String path = in.readUTF();
		int pos_x = in.readInt();
		int pos_y = in.readInt();
		map = (Map) in.readObject();
		player = new DrawableEntity(path, pos_x, pos_y, true);

		
		selectRectangle = new SelectRectangle(this);
	}

	
	/**
	 * Kigenerálja a textureList alapján a megfelelõ komponenst.
	 * @return generalt komponens :{@link JComboBox}
	 */
	public JComboBox GenerateTextureListComponent() {
		return guiManager.GenerateTextureListComponent(textureList.size(), this);
	}

	/**
	 * Kigenerálja a Entity-k alapján a megfelelõ komponenst.
	 * @return ret : {@link JComboBox} -  a kigenerált combo Box
	 */
	public JComboBox GenerateEntityTypeComponent() {
		return guiManager.GenerateEntityTypeComponent(this);
	}

	/**
	 * 
	 * @return map :  {@link Map}
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * 
	 * @return location : {@link Point}
	 */
	public Point GetPlayerLocation() {
		return player.GetLocation();
	}

	/**
	 * Override-olt actionPerformed függvény, vizsgálja a {@link MapEditor} swing elemeinek változását
	 * és beállitja ezek alapján a megfelelõ adatokat a sprite alkotáshoz.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getActionCommand().equals("comboBoxChanged")) {
			guiManager.updateGuiState(arg0);
			JComboBox c = (JComboBox) arg0.getSource();

			if (c.getName().equals("Textures")) {
				//ha a texturákat változtattuk, a sprite_index-t kell beállitani
				sprite_index = c.getSelectedIndex();

			} else if (c.getName().equals("EntityType")) {
				//az entitás tipusát változtattuk, az entityType-ot kell beállitani
				String type = (String) c.getSelectedItem();

				if (type.equals("BLOCK")) {

					entityType = EntityType.BLOCK;

				} else if (type.equals("TRIGGER")) {
					
					entityType = EntityType.TRIGGER;
					//TODO: bug gyanús kód
					JComboBox cb = (JComboBox) guiManager.getTriggerTypeBox();
					triggerType = guiManager.getTriggerType(cb);
				    

				}
			} else if (c.getName().equals("TriggerType")) {
				//a trigger tipusát kell beállitani
				triggerType = guiManager.getTriggerType(c);
			}

		} else {
			JComponent c = (JComponent) arg0.getSource();
			if (c.getName().equals("MessageBoxField")) {
				//A messageBox szövegét kell beállitani
				JTextField t = (JTextField) c;
				MessageString = new String(t.getText());

			}
		}
	}

	/**
	 * Kigenerálja a TriggerList alapján az adott components a guiManagerrel.
	 * @return komponens : {@link Component}
	 */
	public Component GenerateTriggerListComponent() {
		return guiManager.GenerateTriggerListComponent(this);
	}

	/**
	 * 
	 * @return inputHandler : {@link InputHandler}
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}

	/**
	 * 
	 * @return BLOCK_SIZE : int
	 */
	public int getBlockSize() {
		return BLOCK_SIZE;
	}

	public SpriteType getSpriteType() {
		return spriteType;
	}
	public int getSpriteIndex(){
		return sprite_index;
	}
}
