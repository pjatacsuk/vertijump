package com.vjmp.entities.drawable;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.vjmp.entities.Entity;
import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.Sprite.Dir;
import com.vjmp.gfx.Sprite.SpriteType;

/**
 * Olyan entit�s ami kirajz�lhat�
 * Az entity lesz�rmazotja, implement�lja a Serializable interface-t.
 *
 */
public class DrawableEntity extends Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Sprite sprite;
	
	
	/**
	 * 
	 * @param string : {@link String} - a sprite �tvonala
	 * @param x : int - a sprite x pozici�ja
	 * @param y : int - a sprite y pozici�ja
	 * @param visible : boolean - a sprite l�that�s�ga
	 */
	public DrawableEntity(String string, int x, int y, boolean visible) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(string,x,y,visible);
	}

	/**
	 * 
	 * @param path : {@link String} - a sprite �tvonala
	 * @param rect : {@link Rectangle} - a sprite Rectangle-je
	 * @param visible : boolean - a sprite l�that�s�ga
	 */
	public DrawableEntity(String path, Rectangle rect, boolean visible) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(path,rect,visible);
	}

	
	/**
	 * 
	 * @param path : {@link String} - a sprite �tvonala
	 * @param rect : {@link Rectangle} - a sprite Rectangle-je
	 * @param visible : boolean - a sprite l�that�s�ga
	 * @param walls : boolean[] - a sprite falainak t�mbje
	 * @param spriteType : {@link SpriteType} - a sprite tipusa
	 */
	public DrawableEntity(String path, Rectangle rect, boolean visible,
			boolean[] walls, SpriteType spriteType) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(path,rect,visible,walls,spriteType);
	}

	/**
	 * A kirajzol�st v�gzi
	 * @param g : {@link Graphics}
	 */
	public void draw(Graphics g) {
		sprite.draw(g);
	}
	
	/**
	 * Serializ�ci�
	 * @param stream {@link ObjectOutputStream}
	 * @throws IOException 
	 */
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		
		 		stream.writeObject(entityType);
		 		stream.writeObject(sprite);
		 		
	}
	 
	 /**
	  * Deserializ�ci�
	  * @param in : {@link ObjectInputStream}
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		 entityType = (EntityType)in.readObject();
		sprite = (Sprite)in.readObject();
		
	}
	 
	 /**
	  * Be�llitja a l�that�s�got
	  * @param visibility : boolean - l�that�s�g
	  */
	public void setVisibility(boolean visibility) {
		sprite.setVisibility(visibility);
	}

	/**
	 * Be�llitja a sprite pozici�j�t
	 * @param x : int - x pozici�
	 * @param y : int - y pozici�
	 */
	public void setLocation(int x, int y) {
		sprite.setLocation(x, y);
		
	}

	/**
	 * Visszat�r a sprite X pozici�j�val
	 * @return x : int - x pozici�
	 */
	public int GetPosX() {
		return sprite.GetPosX();
	}

	/**
	 * Visszat�r a sprite Y pozici�j�val
	 * @return y : int - y pozici�
	 */
	public int GetPosY() {
		return sprite.GetPosY();
	}

	/**
	 * Visszat�r a sprite pozici�j�val
	 * @return point : {@link Point} - sprite pozici�
	 */
	public Point GetLocation() {
		return sprite.GetLocation();
	}

	/**
	 * Visszat�r a sprite rectangle magass�g�val
	 * @return spriteRectHeight : int - a sprite rectangle magass�ga
	 */
	public int GetHeight() {
		return sprite.getRectHeight();
	}

	/**
	 * Visszat�r a sprite rectanglj�vel
	 * @return spriteRect : {@link Rectangle} - a sprite rectanglje
	 */
	public Rectangle getRect() {
		return sprite.getRect();
	}
	
	/**
	 * Rectangle �tk�z�st vizsg�l
	 * @param rect : a vizsg�land� {@link Rectangle}
	 * @return true - ha van �tk�z�s, false - ha nincs
	 */
	public boolean intersects(Rectangle rect){
		return sprite.getRect().intersects(rect);
	}

	/**
	 * Visszat�r a sprite l�that�s�g�val
	 * @return visibility : boolean - true ha l�that� , false ha nem
	 */
	public boolean isVisible() {
		return sprite.isVisible();
	}

	/**
	 * Visszat�r a megadott direction alapj�n az adott oldal fal-e
	 * @param dir : {@link Dir} - a megadott direction
	 * @return boolean : true ha fal van a megadott direction alapj�n, false ha nincs
	 */
	public boolean getWall(Dir dir) {
		return	sprite.GetWall(dir);
	}

	/**
	 * Be�llitja a sprite tipus�t a megadott {@link SpriteType} alapj�n
	 * @param spriteType : {@link SpriteType}
	 */
	public void setSpriteType(SpriteType spriteType) {
		sprite.setSpriteType(spriteType);
	}
}
