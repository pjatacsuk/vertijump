package com.vjmp;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

import com.vjmp.entities.drawable.DrawableEntity;
import com.vjmp.gfx.Camera;
import com.vjmp.managers.DrawableEntityManager;

public class Map implements Iterable<DrawableEntity>,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	int	WIDTH;
	private int HEIGHT;
	private DrawableEntityManager entityManager;
	private boolean isEditor = false;
	
	public Map(int width,int height) {
		entityManager = new DrawableEntityManager();
		WIDTH = width;
		HEIGHT= height;
	//	GenerateMap(100);
		GenerateTest2(120);
		entityManager.add(new DrawableEntity("./res/debug_platform.png",0,HEIGHT-25,true));
	}
	public Map(int width,int height,boolean iseditor) {
		entityManager = new DrawableEntityManager();
		WIDTH = width;
		HEIGHT= height;
		isEditor = iseditor;
	//	GenerateMap(100);
	//	GenerateTest2(120);
		entityManager.add(new DrawableEntity("./res/debug_platform.png",0,HEIGHT-25,true));
	}
	
	public Map(Map map,boolean isEditor) {
		entityManager = new DrawableEntityManager(map.entityManager);
		WIDTH = map.WIDTH;
		HEIGHT = map.HEIGHT;
		this.isEditor = isEditor;
	}
	public void draw(Graphics g) {
		entityManager.DrawSprites(g);
	}

	public void GenerateMap(int n) {
		
		for(int i=0;i<n;i++) {
			
			entityManager.add(GenerateDrawableEntity(entityManager.getLast(),i));
		}
	}
	
	public DrawableEntity GenerateDrawableEntity(DrawableEntity drawable,int i) {
		int block_space_width = 150;
		int block_space_height = 100;
		
		int pos_x;
		int pos_y;
		boolean visibility = true;
		if(drawable != null) { 
			pos_x = drawable.GetPosX();
			pos_y = drawable.GetPosY();
		} else {
			pos_x = 0;
			pos_y = 0;
		}
			
		int rnd_pos_x = (int)(Math.random()*WIDTH);
		int rnd_pos_y = (int)(Math.random()*HEIGHT - i*block_space_height-200);
		
		if(Math.abs(rnd_pos_x - pos_x) < block_space_width) {
			if(Math.random() < 0.5) {
				if(rnd_pos_x - block_space_width > 0) {
					rnd_pos_x -= block_space_width;
				} else {
					rnd_pos_x += block_space_width;
				} 
			} else {
				if(rnd_pos_x + block_space_width < WIDTH ) {
					rnd_pos_x += block_space_width;
				} else {
					rnd_pos_x -= block_space_width;
				}
			}
		}
		
		if(rnd_pos_y < 0) visibility = false;
		return new DrawableEntity("./res/block.png",rnd_pos_x,rnd_pos_y,visibility);
	}
	
	private void GenerateTest1(int n) {
	//	spriteManager.add(new Sprite("./res/block.png",40,HEIGHT-200,true));
	//	spriteManager.add(new Sprite("./res/block.png",140,HEIGHT-300,true));
	//	spriteManager.add(new Sprite("./res/block.png",240,HEIGHT-400,true));
	//	spriteManager.add(new Sprite("./res/block.png",340,HEIGHT-500,true));
		int dir = -1;
		for(int i=0;i<n;i++) {
			if(i%4 == 0) dir = dir * -1;
			if(dir == 1) {
				entityManager.add(new DrawableEntity("./res/block.png",40+(i%4)*110,HEIGHT-100-i*100,true));
			} else {
				entityManager.add(new DrawableEntity("./res/block.png",500-(40+(i%4)*110),HEIGHT-100-i*100,true));
			}
		}
		
	}
	public void GenerateTest2(int n) {
		for(int j=0;j<n;j++) {
			Rectangle rect1 = new Rectangle(100,HEIGHT-100-j*100,25+10,100+10);
			Rectangle rect2 = new Rectangle(400,HEIGHT-100-j*100,25+10,100+10);
			entityManager.add(new DrawableEntity("./res/block.png",rect1,true));
			entityManager.add(new DrawableEntity("./res/block.png",rect2,true));
		}
		Rectangle rect = new Rectangle(200,200,30,30);
		entityManager.add(new DrawableEntity("./res/block_blue.png",rect,true));
	}
	
	@Override
	public Iterator<DrawableEntity> iterator() {
		return entityManager.iterator();
	}
	public void update(Camera camera) {
		if(!isEditor)	{
			UpdateSprites(camera.pos_y);
		}	else {
			UpdateEditorSprites(camera.pos_y);
		}
	}
	


	private void UpdateEditorSprites(int pos_y) {
		for(int i=0;i<entityManager.size();i++) {
			DrawableEntity tmp = entityManager.get(i);
			 if(tmp.GetPosY() + tmp.GetHeight() < 0 - pos_y) {
				tmp.setVisibility(true);
			}
		}
		
	}
	public void UpdateSprites(int CameraY) {
		for(int i=0;i<entityManager.size();i++) {
			DrawableEntity tmp = entityManager.get(i);
			if(tmp.GetPosY() > -CameraY + HEIGHT) {
				entityManager.remove(i);
			} else if(tmp.GetPosY() + tmp.GetHeight() < 0 - CameraY) {
				tmp.setVisibility(true);
			}
		}
	}
	
	public void add(DrawableEntity sprite) {
		entityManager.add(sprite);
	}
	
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		stream.writeInt(WIDTH);
		stream.writeInt(HEIGHT);
		stream.writeBoolean(isEditor);
		 stream.writeObject(entityManager);
		 
		 
	 }
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		
		 System.out.println("map_read");
		 WIDTH = in.readInt();
		 HEIGHT = in.readInt();
		 isEditor = in.readBoolean();
		 entityManager = (DrawableEntityManager)in.readObject();  
		 
	 }
	public void remove(Rectangle rect) {
		entityManager.remove(rect);
	}
	 
}
