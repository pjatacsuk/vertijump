package com.vjmp.gfx;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public class AnimatedSprite implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum AnimType{
		LOOP,ONCE
	};
	
	List<Sprite> spriteList = null;
	public static int frame_per_sprite = 10;
	private int current_sprite = 0;
	private int tick = 0;
	private int sprite_count = 0;
	private int anim_width = 0;
	private int offSet = 0;
	private AnimType animType = AnimType.ONCE;
	private String path = null;
	
	public AnimatedSprite(String path,int anim_width,int offSet){
		spriteList = new ArrayList<Sprite>();
		Sprite tmp = new Sprite(path);
		this.path = path;
		this.anim_width = anim_width;
		this.offSet = offSet;
		sprite_count = tmp.getImgWidth() / (anim_width + offSet);
		for(int i=0;i<sprite_count;i++){
			spriteList.add(new Sprite(tmp.getSubImg((anim_width+offSet)*i, 0,anim_width,tmp.getImgHeight()),
									  path));
		}
	}
	
	public AnimatedSprite(BufferedImage img,int anim_width,int offSet){
		spriteList = new ArrayList<Sprite>();
		sprite_count = img.getWidth() / (anim_width + offSet);
		for(int i=0;i<sprite_count;i++){
			spriteList.add(new Sprite(img.getSubimage((anim_width+offSet)*i, 0,anim_width,img.getHeight()),
							path));
		}
	}
	public void loadSprites() {
		spriteList = new ArrayList<Sprite>();
		Sprite tmp = new Sprite(path);
		sprite_count = tmp.getImgWidth() / (anim_width + offSet);
		for(int i=0;i<sprite_count;i++){
			spriteList.add(new Sprite(tmp.getSubImg((anim_width+offSet)*i, 0,anim_width,tmp.getImgHeight()),
									  path));
		}
		
	}
	public void update(){
		tick++;
		if(tick>=frame_per_sprite){
			if(animType == AnimType.ONCE) {
				current_sprite = Math.min(current_sprite++,sprite_count);
				
			} else if(animType == AnimType.LOOP) {
				current_sprite = (current_sprite + 1) % sprite_count;
			}
			tick = 0;
		}
	}
	public void draw(Graphics g){
		spriteList.get(current_sprite).draw(g);
		
	}

	public void reset() {
		current_sprite = 0;
		
	}
	public void move(double x,double y) {
		for(Sprite sprite : spriteList){
			sprite.move(x, y);
		}
	}

	public void setLocation(int x, int y) {
		for(Sprite sprite : spriteList){
			sprite.setLocation(x, y);
		}	
	}
	public void setAnimType(AnimType animType){
		this.animType = animType;
	}
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		stream.writeInt(frame_per_sprite);
		stream.writeInt(sprite_count);
		stream.writeObject(animType);
		stream.writeInt(anim_width);
		stream.writeInt(offSet);
		stream.writeUTF(path);
		stream.writeInt(spriteList.get(0).GetPosX());
		stream.writeInt(spriteList.get(0).GetPosY());
	 }
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		frame_per_sprite = in.readInt();
		sprite_count = in.readInt();
		animType = (AnimType)in.readObject();
		anim_width = in.readInt();
		offSet = in.readInt();
		path = in.readUTF();
		loadSprites();
		int pos_x = in.readInt();
		int pos_y = in.readInt();
		setLocation(pos_x,pos_y);
	 }

	public int getHeight() {
		return spriteList.get(0).getImgHeight();
		
	}

	public int getWidth() {
		return spriteList.get(0).getImgWidth();
	}
}
