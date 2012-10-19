package com.vjmp;

import java.awt.Graphics;
import java.util.Iterator;

import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.SpriteManager;

public class Map implements Iterable<Sprite> {
	private	int	WIDTH;
	private int HEIGHT;
	private SpriteManager spriteManager;
	
	
	public Map(int width,int height) {
		spriteManager = new SpriteManager();
		WIDTH = width;
		HEIGHT= height;
	//	GenerateMap(100);
		GenerateTest1(120);
		spriteManager.add(new Sprite("./res/debug_platform.png",0,HEIGHT-25,true));
	}
	
	public void draw(Graphics g) {
		spriteManager.DrawSprites(g);
	}

	public void GenerateMap(int n) {
		
		for(int i=0;i<n;i++) {
			
			spriteManager.add(GenerateSprite(spriteManager.getLast(),i));
		}
	}
	public Sprite GenerateSprite(Sprite sprite,int i) {
		int block_space_width = 150;
		int block_space_height = 100;
		
		int pos_x;
		int pos_y;
		boolean visibility = true;
		if(sprite != null) { 
			pos_x = sprite.GetPosX();
			pos_y = sprite.GetPosY();
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
		return new Sprite("./res/block.png",rnd_pos_x,rnd_pos_y,visibility);
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
				spriteManager.add(new Sprite("./res/block.png",40+(i%4)*110,HEIGHT-100-i*100,true));
			} else {
				spriteManager.add(new Sprite("./res/block.png",500-(40+(i%4)*110),HEIGHT-100-i*100,true));
			}
		}
		
	}
	
	@Override
	public Iterator<Sprite> iterator() {
		return spriteManager.iterator();
	}
	public void update(int CameraY) {
		UpdateSprites(CameraY);
	}
	


	public void UpdateSprites(int CameraY) {
		for(int i=0;i<spriteManager.size();i++) {
			Sprite tmp = spriteManager.get(i);
			if(tmp.GetPosY() > -CameraY + HEIGHT) {
				spriteManager.remove(i);
			} else if(tmp.GetPosY() + tmp.GetHeight() < 0) {
				tmp.setVisible(true);
			}
		}
	}
}
