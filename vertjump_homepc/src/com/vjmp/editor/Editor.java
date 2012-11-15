package com.vjmp.editor;

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
import com.vjmp.managers.GuiManager;
public class Editor implements Serializable,ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int  BLOCK_SIZE = 15;
	private static int sprite_index = 0;
	private static EntityType entityType = EntityType.BLOCK;
	private static DrawableEntity player = null;
	private static InputHandler.Buttons mouse_button = Buttons.NONE;
	private static String MessageString = null;
	private static TriggerType triggerType;
		
	private Map map = null;
	private TextureList textureList = null;
	private boolean polling_for_mouse_release = true;
	private SelectRectangle selectRectangle = null;
	private GuiManager guiManager = null;
	
	private Point old_camera_pos = null;
	
	transient private InputHandler inputHandler=null;
	
	
	public Editor(InputHandler inputHandler,int WIDTH,int HEIGHT) {
		map = new Map(WIDTH,HEIGHT,true);
		this.inputHandler = inputHandler; 
		textureList = new TextureList();
		selectRectangle = new SelectRectangle(this);
	}
	public void setInputHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
	public void setComponentManager(GuiManager componentManager){
		this.guiManager = componentManager;
	}
	public void addSprite(Rectangle rect) {
			
	    if(textureList.GetPath(sprite_index).equals("./res/spooky.png")){
	    	if(player == null) {
	    		player = new DrawableEntity("./res/spooky.png",rect.x,rect.y,true);
	    	} else {
	    		player.setLocation(rect.x,rect.y);
	    	}
	    }
	    else {
	    	DrawableEntity drawableEntity = GetDrawableEnityTypeFromSpriteIndex(rect);
	    	if(drawableEntity != null) {
		    	map.add(drawableEntity);
		    } else {
		    	System.out.println("HIBA!");
		    }
	    }
	}
	private DrawableEntity GetDrawableEnityTypeFromSpriteIndex(Rectangle rect) {
		
		DrawableEntity ret = null;
		switch(entityType) {
		case BLOCK:
			ret = new DrawableEntity(textureList.GetPath(sprite_index),rect,true,guiManager.getWalls());
			break;
		case TRIGGER:
			ret = GetTriggerEntityFromType(rect);
			break;
		}
		
		return ret;
	
	}
	private DrawableEntity GetTriggerEntityFromType(Rectangle rect) {
		TriggerEntity ret = null;
		switch(triggerType) {
		case SPIKE:
			ret = new SpikeTrigger(textureList.GetPath(sprite_index),rect,true,guiManager.getWalls());
			break;
		case FINISH_LINE:
			ret = new FinishLine(textureList.GetPath(sprite_index),rect,true);
			break;
		case MESSAGE_BOX:
			if(MessageString != null){
				ret = new MessageBox(MessageString,textureList.GetPath(sprite_index),rect,true,guiManager.getWalls());
			}
			break;
		}
		return ret;
	}
	public void update(Camera camera) {
		map.update(camera);
		BlockLogic(camera);
		if(guiManager != null) {
			guiManager.update();
		}
		selectRectangle.update(camera);
	}
	
	private void BlockLogic(Camera camera) {
		if(inputHandler.MOUSE.button.isPressed()) {
			polling_for_mouse_release = true;
			if(old_camera_pos == null){
				old_camera_pos  = new Point(camera.pos_x,camera.pos_y);
			}
			if(inputHandler.MOUSE.button_clicked == Buttons.LEFT) {
				mouse_button = Buttons.LEFT;
				
			} else if(inputHandler.MOUSE.button_clicked == Buttons.RIGHT) {
				mouse_button = Buttons.RIGHT;
			}
		}
		if(polling_for_mouse_release) {
			if(!inputHandler.MOUSE.button.isPressed()){
				polling_for_mouse_release = false;
				
				if(mouse_button == Buttons.LEFT) {
					
					Rectangle rect = GenerateRectangleFromMouse(camera);
					addSprite(rect);
				} else if(mouse_button == Buttons.RIGHT){
					
					Rectangle rect = GenerateRectangleFromMouse(camera);
					removeSprite(rect);
				}
				//mar nincs ra szukseg, mivel legeneraltuk a rectangle-t a remove,illetve add-hoz
				old_camera_pos = null;
			}
			
		}
		
	}
	
	private void removeSprite(Rectangle rect) {
		map.remove(rect);
		
		
	}
	public  Rectangle GenerateRectangleFromMouse(Camera camera) {
		
		//camera positionje mindig mod15 == 0, mivel ennyi az editor speedje, ezert
		// csak a mouse position-t kell div15 ölni
		Point old_pos =AddOldCameraPos(DivBlockSize(inputHandler.MOUSE.getOldPos()));
		Point pos = AddCameraPos(DivBlockSize(inputHandler.MOUSE.getPos()),camera);
		int width = Math.abs(old_pos.x - pos.x) + BLOCK_SIZE;
		int height = Math.abs(old_pos.y - pos.y) +BLOCK_SIZE;
		
		int x = Math.min(old_pos.x, pos.x);
		int y = Math.min(old_pos.y, pos.y);
		
	
		return new Rectangle(x,y,width,height);
		
	}
	private Point AddOldCameraPos(Point p) {
		p.x = p.x - old_camera_pos.x;
		p.y = p.y - old_camera_pos.y;
		return p;
	}
	public Point AddCameraPos(Point p,Camera c) {
		p.x = p.x - c.pos_x;
		p.y = p.y - c.pos_y;;
		return p;
	}
	public Point DivBlockSize(Point p) {
		return new Point(p.x - (p.x % BLOCK_SIZE),p.y - (p.y % BLOCK_SIZE));
	}

	public void draw(Graphics g) {
		map.draw(g);
		if(player!=null) player.draw(g);
		selectRectangle.draw(g);
	}

	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		 if(player == null) {
			 System.out.println("Nincs spawnpoint");
			 return;
		 }
		 System.out.println("serialization");
		 stream.writeUTF("./res/spooky.png");
		 stream.writeInt(player.GetPosX());
		 stream.writeInt(player.GetPosY());
		 stream.writeObject(map);
		 
	 }
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		
		 String path = in.readUTF();
		 int	pos_x = in.readInt();
		 int	pos_y = in.readInt();
		 map = (Map)in.readObject();
		 player = new DrawableEntity(path,pos_x,pos_y,true);
		 textureList = new TextureList();
		selectRectangle = new SelectRectangle(this);
	 }
	 public JComboBox GenerateTextureListComponent() {
		 return guiManager.GenerateTextureListComponent(textureList.size(), this);
	 } 
	 public JComboBox GenerateEntityTypeComponent() {
		  return guiManager.GenerateEntityTypeComponent(this);
	 }
	public Map getMap() {
		return map;
	}
	public Point GetPlayerLocation() {
		return player.GetLocation();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(arg0.getActionCommand().equals("comboBoxChanged")) {
			guiManager.updateGuiState(arg0);
			JComboBox c = (JComboBox)arg0.getSource();
			if(c.getName().equals("Textures")) {
				sprite_index = c.getSelectedIndex();
			} else if(c.getName().equals("EntityType")) {
				String type = (String) c.getSelectedItem();
				if(type.equals("BLOCK")) {
					entityType = EntityType.BLOCK;
				} else if(type.equals("TRIGGER")) {
					entityType = EntityType.TRIGGER;
					JComboBox cb = (JComboBox) guiManager.getTriggerTypeBox();	
					triggerType = guiManager.getTriggerType(cb);
				}
			} else if(c.getName().equals("TriggerType")) {
				triggerType = guiManager.getTriggerType(c);
			}
			
		} else  {
			JComponent c = (JComponent)arg0.getSource();
			if(c.getName().equals("MessageBoxField")) {
				JTextField t = (JTextField)c;
				MessageString = new String(t.getText());
				
			}
		}
	}
	public Component GenerateTriggerListComponent() {
		return guiManager.GenerateTriggerListComponent(this);
	}
	public InputHandler getInputHandler(){
		return inputHandler;
	}
	public int getBlockSize(){
		return BLOCK_SIZE;
	}
}
