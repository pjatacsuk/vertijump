package com.vjmp.editor;

import java.awt.Dimension;
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

import com.vjmp.InputHandler;
import com.vjmp.InputHandler.Buttons;
import com.vjmp.Map;
import com.vjmp.entities.drawable.DrawableEntity;
import com.vjmp.entities.drawable.FinishLine;
import com.vjmp.gfx.Camera;
import com.vjmp.gfx.Sprite;
public class Editor implements Serializable,ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map map= null;
	private static int sprite_index = 0;
	private static DrawableEntity player = null;
	private TextureList textureList = null;
	transient private InputHandler inputHandler=null;
	private static final int  BLOCK_SIZE = 15;
	transient private Point old_mouse_coord = null;
	
	private boolean polling_for_mouse_release = true;
	private static InputHandler.Buttons mouse_button = Buttons.NONE;
	
	
	public Editor(InputHandler inputHandler,int WIDTH,int HEIGHT) {
		map = new Map(WIDTH,HEIGHT,true);
		this.inputHandler = inputHandler; 
		textureList = new TextureList();
		
	}
	public void setInputHandler(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}
	
	public void addSprite(Rectangle rect) {
	
		
	    if(textureList.GetPath(sprite_index).equals("./res/spooky.png")){
	    	if(player == null) {
	    		player = new DrawableEntity("./res/spooky.png",rect.x,rect.y,true);
	    	} else {
	    		player.setLocation(rect.x,rect.y);
	    	}
	    }
	    else if(textureList.GetPath(sprite_index).equals("./res/block_brown.png")) {
	    	map.add(new FinishLine(textureList.GetPath(sprite_index),rect,false));
	    	
	    }  else {
		map.add(new DrawableEntity(textureList.GetPath(sprite_index),rect,true));
	    }
	}
	public void update(Camera camera) {
		map.update(camera);
		BlockLogic(camera);
	
	}
	
	private void BlockLogic(Camera camera) {
		if(inputHandler.MOUSE.button.isPressed()) {
			polling_for_mouse_release = true;
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
			}
		}
		
	}
	
	private void removeSprite(Rectangle rect) {
		map.remove(rect);
		
	}
	public Rectangle GenerateRectangleFromMouse(Camera camera) {
		Point old_pos =DivBlockSize(AddCameraPos(inputHandler.MOUSE.GetOldPos(),camera));
		Point pos = DivBlockSize(AddCameraPos(inputHandler.MOUSE.GetPos(),camera));
		int width = Math.abs(old_pos.x - pos.x) + BLOCK_SIZE;
		int height = Math.abs(old_pos.y - pos.y) +BLOCK_SIZE;
		
		int x = Math.min(old_pos.x, pos.x);
		int y = Math.min(old_pos.y, pos.y);
		
	
		return new Rectangle(x,y,width,height);
		
	}
	public Point AddCameraPos(Point p,Camera c) {
		p.x = p.x - c.pos_x * 2;
		p.y = p.y - c.pos_y * 2;
		return p;
	}
	public Point DivBlockSize(Point p) {
		return new Point((p.x / BLOCK_SIZE) * BLOCK_SIZE,
						 (p.y / BLOCK_SIZE) * BLOCK_SIZE);
	}

	public void draw(Graphics g) {
		map.draw(g);
		if(player!=null) player.draw(g);
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
	 }
	 public JComboBox GenerateTextureListComponent() {
		    String textures_temp = "";
		    for(int i=0;i<textureList.size();i++) {
		    	textures_temp = textures_temp + i + " ";
		    }
		    String[] textures = textures_temp.split(" ");
		    
			JComboBox list = new JComboBox(textures);
			ComboBoxRenderer renderer = new ComboBoxRenderer();
			list.setRenderer(renderer);
			list.setPreferredSize(new Dimension(100,100));
			list.addActionListener(this);
			return list;
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
			JComboBox c = (JComboBox)arg0.getSource();
			sprite_index = c.getSelectedIndex();
		 
			
		}
		
	}
	
	
	 


}
