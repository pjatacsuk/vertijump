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

/**
 * Az {@link AnimatedSprite} olyan spriteok sorozata, amelyek egymás után animációnak tekinthetõ.
 * Az {@link AnimatedSprite} felelõs ezen sprite-ok rendezésért, lejátszásáért, frissitéséért.
 *
 */
public class AnimatedSprite implements Serializable {

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
	
	/**
	 * Konstruktor
	 * @param path : {@link String} - a sprite-okat tartalmazó kép elérési útvonala
	 * @param anim_width : int - az egyes sprite-ok szélessége
	 * @param offSet : int - az egyes sprite-ok vége és a rákövetkezõ sprite eleje közötti különbség
	 */
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
	/**
	 * 
	 * @param img : {@link BufferedImage} - a sprite-okat tartalmazó kép
	 * @param anim_width : int - az egyes sprite-ok szélessége
	 * @param offSet : int - az egyes sprite-ok vége és a rákövetkezõ sprite eleje közötti különbség
	 */
	public AnimatedSprite(BufferedImage img,int anim_width,int offSet){
		spriteList = new ArrayList<Sprite>();
		sprite_count = img.getWidth() / (anim_width + offSet);
		for(int i=0;i<sprite_count;i++){
			spriteList.add(new Sprite(img.getSubimage((anim_width+offSet)*i, 0,anim_width,img.getHeight()),
							path));
		}
	}
	
	/**
	 * Betölti a sprike-okat a megadott útvonal alapján a listába
	 */
	public void loadSprites() {
		spriteList = new ArrayList<Sprite>();
		Sprite tmp = new Sprite(path);
		sprite_count = tmp.getImgWidth() / (anim_width + offSet);
		for(int i=0;i<sprite_count;i++){
			spriteList.add(new Sprite(tmp.getSubImg((anim_width+offSet)*i, 0,anim_width,tmp.getImgHeight()),
									  path));
		}
		
	}
	
	/**
	 * Update-el, ami azt jelenti jelen esteben, hogy kiválassza a megjelenitendõ sprite-ot az {@link AnimType} függvényében.
	 * Ha az {@link AnimType} "loop" értékre van állitva, akkor ismételgeti az animációt,
	 * ha az {@link AnimType} "once" értékre van állitva, akkor csak egyszer játsza le az animációt
	 */
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
	
	/**
	 * Kirajzolja a megjelenitendõ sprite-ot
	 * @param g : {@link Graphics}
	 */
	public void draw(Graphics g){
		spriteList.get(current_sprite).draw(g);
		
	}

	/**
	 * Reseteli a megjelenitendõ sprite-ok sorszámát
	 */
	public void reset() {
		current_sprite = 0;
		
	}
	/**
	 * Elmozgatja a sprite-ot a megadott mértékkel
	 * @param x : double - x tengely mentén való eltolás mértéke
	 * @param y : double - y tengely mentén való eltolás mértéke
	 */
	public void move(double x,double y) {
		for(Sprite sprite : spriteList){
			sprite.move(x, y);
		}
	}

	/**
	 * Elmozgatja a sprite-ot a megadott helyre
	 * @param x : int - a sprite x poziciója
	 * @param y : int - a sprite y poziciója
	 */
	public void setLocation(int x, int y) {
		for(Sprite sprite : spriteList){
			sprite.setLocation(x, y);
		}	
	}
	
	/**
	 * Beállitja az animáció tipusát
	 * @param animType : {@link AnimType} az érték amire beállitjuk
	 */
	public void setAnimType(AnimType animType){
		this.animType = animType;
	}
	
	/**
	 * Serializáció
	 * @param stream {@link ObjectOutputStream}
	 * @throws IOException
	 */
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
	
	/**
	 * Deserializáció
	 * @param in {@link ObjectInputStream}
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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

	 
	 /**
	  * Megadja a sprite képének magasságát
	  * @return ret : int - sprite képének magasságas
	  */
	public int getHeight() {
		return spriteList.get(0).getImgHeight();
		
	}

	/**
	 * Megadja a sprite képének szélességét
	 * @return ret : int - sprite képének szélessége
	 */
	public int getWidth() {
		return spriteList.get(0).getImgWidth();
	}
}
