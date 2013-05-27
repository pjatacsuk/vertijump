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
 * A szerkeszt� fel�let, feladatai minden fajta szerkeszt�i funkci�. DrawableEntity-ket, {@link TriggerEntity}-ket
 * helyez el, illetve t�r�l a p�ly�n.
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
	 * @param inputHandler : {@link InputHandler} : az input kezel�
	 * @param WIDTH : int - a szerkeszt� sz�less�ge
	 * @param HEIGHT : int - a szerkeszt� magass�ga
	 */
	public Editor(InputHandler inputHandler, int WIDTH, int HEIGHT) {
		map = new Map(WIDTH, HEIGHT, true);
		this.inputHandler = inputHandler;

		selectRectangle = new SelectRectangle(this);
	}

	/**
	 * Be�llitja az inputHandler-t
	 * @param inputHandler : {@link InputHandler}
	 */
	public void setInputHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	/**
	 * Be�llitja a GuiManager-t.
	 * @param componentManager : {@link GuiManager}
	 */
	public void setComponentManager(GuiManager componentManager) {
		this.guiManager = componentManager;
	}

	/**
	 * Hozz�adja a megfelel� entit�st a p�ly�hoz a rect {@link Rectangle} alapj�n.
	 * A current_sprite_index alapj�n meghat�rozza az entit�s text�r�j�t
	 * @param rect
	 */
	public void addSprite(Rectangle rect) {
		
		//Kezdetleges megold�s, spawn pointot a spooky.png k�ppel kiv�lasztva rakhatunk le
		//TODO:Refactor triggerr�
		if (textureList.GetPath(sprite_index).equals("./res/png/spooky.png")) {
			if (player == null) {
				player = new DrawableEntity("./res/png/spooky.png", rect.x, rect.y,
						true);
			} else {
				player.setLocation(rect.x, rect.y);
			}
		} else {
			//L�trehozzuk a az entit�st a spriteType,entityType,rectangle,text�ra alapj�n
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
	 * @param rect : {@link Rectangle} - a hozz�adand� entit�s {@link Rectangle}-je
	 * @return ret : {@link DrawableEntity} - a megalkotott entit�s.
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
	 * Az editor a kapott adatok alapj�n megalkotja a megfelel� {@link TriggerEntity}-t.
	 * 
	 * @param rect : {@link Rectangle} - a hozz�adand� entit�s {@link Rectangle}-je
	 * @return ret : {@link DrawableEntity} - val�j�ban egy TriggerEntity, amit a kapott adatok alapj�n
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
	 * Frissiti az editort. K�zt�k a {@link Map}-t,a {@link SelectRectangle}-t.
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
	 * Az inputHandler alapj�n frissiti az alkotand� entit�s {@link SpriteType}-j�t,
	 * a mouse mozg�sa alapj�n pedig meghat�rozza a DrawableEntity {@link Rectangle}-j�t.
	 * @param camera
	 */
	private void BlockLogic(Camera camera) {
		if (inputHandler.MOUSE.button.isPressed()) {
			polling_for_mouse_release = true;
			//kell egy r�gi kamera �ll�s a helyes rectangle sz�mit�s miatt
			//ha mozgott k�zbe a kamera, sz�ks�g van r�
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
					
					//kisz�mitjuk a megfelel� rectangle-t a r�gi �s �j kamera �ll�s alapj�n
					
					Rectangle rect = GenerateScaledRectangleFromMouse(camera);
					if(rect == null) {
						System.out.println("HIBA,rect == null!");
					} else {
						//minden ok�, mehet az entit�s
						addSprite(rect);
					}
				} else if (mouse_button == Buttons.RIGHT) {

					//t�rl�st v�gezz�k
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
	 * A {@link Rectangle} alapj�n t�rli az ezzel �tk�z� entit�sokat.
	 * @param rect : {@link Rectangle} - a mouse �ltal meghat�rozott {@link Rectangle}.
	 */
	private void removeSprite(Rectangle rect) {
		map.remove(rect);

	}

	/**
	 * A mouse gomb lenyom�sa alatt meghat�rozott {@link Rectangle}-t gener�lja ki. 
	 * @param camera : {@link Camera}
	 * @return ret : {@link Rectangle} - a kigener�lt {@link Rectangle}
	 */
	public Rectangle GenerateScaledRectangleFromMouse(Camera camera) {

		// camera positionje mindig mod15 == 0, mivel ennyi az editor speedje,
		// ezert
		// csak a mouse position-t kell div15 �lni
		
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
	 * Meghat�rozza az ezel�tti mouse esem�ny sor�n keletkezett mouse pozici�t.
	 * @param p : {@link Point} - a jelenlegi mouse pozici�.
	 * @return
	 */
	private Point AddOldCameraPos(Point p) {
		p.x = p.x - old_camera_pos.x;
		p.y = p.y - old_camera_pos.y;
		return p;
	}

	/**
	 * Meghat�rozza a jelenlegi mouse esem�ny sor�n keletkezett mouse pozici�t.
	 * @param p
	 * @param c
	 * @return p : Point - a v�ltoztatott point
	 */
	public Point AddCameraPos(Point p, Camera c) {
		p.x = p.x - c.pos_x;
		p.y = p.y - c.pos_y;
		
		return p;
	}

	/**
	 * A BLOCK_SIZE alapj�n meghat�rozott konstanssal elosztja a p {@link Point}-ot.
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
	 * Kirajzolja a sprite-ok oldalait, az sprite falainak(fal -> ahol �tk�z�s �ll fenn) megfelel�en
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
	 * Visszadja a border szin�t a sprite direction ir�ny� fal�nak megfelel�en
	 * @param sprite : {@link Sprite} - a vizsg�lt sprite
	 * @param dir : {@link Dir} - a vizsg�lt direction
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
	 * Serializ�ci�
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
	 * Deserializ�ci�
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
	 * Kigener�lja a textureList alapj�n a megfelel� komponenst.
	 * @return generalt komponens :{@link JComboBox}
	 */
	public JComboBox GenerateTextureListComponent() {
		return guiManager.GenerateTextureListComponent(textureList.size(), this);
	}

	/**
	 * Kigener�lja a Entity-k alapj�n a megfelel� komponenst.
	 * @return ret : {@link JComboBox} -  a kigener�lt combo Box
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
	 * Override-olt actionPerformed f�ggv�ny, vizsg�lja a {@link MapEditor} swing elemeinek v�ltoz�s�t
	 * �s be�llitja ezek alapj�n a megfelel� adatokat a sprite alkot�shoz.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getActionCommand().equals("comboBoxChanged")) {
			guiManager.updateGuiState(arg0);
			JComboBox c = (JComboBox) arg0.getSource();

			if (c.getName().equals("Textures")) {
				//ha a textur�kat v�ltoztattuk, a sprite_index-t kell be�llitani
				sprite_index = c.getSelectedIndex();

			} else if (c.getName().equals("EntityType")) {
				//az entit�s tipus�t v�ltoztattuk, az entityType-ot kell be�llitani
				String type = (String) c.getSelectedItem();

				if (type.equals("BLOCK")) {

					entityType = EntityType.BLOCK;

				} else if (type.equals("TRIGGER")) {
					
					entityType = EntityType.TRIGGER;
					//TODO: bug gyan�s k�d
					JComboBox cb = (JComboBox) guiManager.getTriggerTypeBox();
					triggerType = guiManager.getTriggerType(cb);
				    

				}
			} else if (c.getName().equals("TriggerType")) {
				//a trigger tipus�t kell be�llitani
				triggerType = guiManager.getTriggerType(c);
			}

		} else {
			JComponent c = (JComponent) arg0.getSource();
			if (c.getName().equals("MessageBoxField")) {
				//A messageBox sz�veg�t kell be�llitani
				JTextField t = (JTextField) c;
				MessageString = new String(t.getText());

			}
		}
	}

	/**
	 * Kigener�lja a TriggerList alapj�n az adott components a guiManagerrel.
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
