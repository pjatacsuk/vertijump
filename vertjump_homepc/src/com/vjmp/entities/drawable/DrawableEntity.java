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
 * Olyan entitás ami kirajzólható
 * Az entity leszármazotja, implementálja a Serializable interface-t.
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
	 * @param string : {@link String} - a sprite útvonala
	 * @param x : int - a sprite x poziciója
	 * @param y : int - a sprite y poziciója
	 * @param visible : boolean - a sprite láthatósága
	 */
	public DrawableEntity(String string, int x, int y, boolean visible) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(string,x,y,visible);
	}

	/**
	 * 
	 * @param path : {@link String} - a sprite útvonala
	 * @param rect : {@link Rectangle} - a sprite Rectangle-je
	 * @param visible : boolean - a sprite láthatósága
	 */
	public DrawableEntity(String path, Rectangle rect, boolean visible) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(path,rect,visible);
	}

	
	/**
	 * 
	 * @param path : {@link String} - a sprite útvonala
	 * @param rect : {@link Rectangle} - a sprite Rectangle-je
	 * @param visible : boolean - a sprite láthatósága
	 * @param walls : boolean[] - a sprite falainak tömbje
	 * @param spriteType : {@link SpriteType} - a sprite tipusa
	 */
	public DrawableEntity(String path, Rectangle rect, boolean visible,
			boolean[] walls, SpriteType spriteType) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(path,rect,visible,walls,spriteType);
	}

	/**
	 * A kirajzolást végzi
	 * @param g : {@link Graphics}
	 */
	public void draw(Graphics g) {
		sprite.draw(g);
	}
	
	/**
	 * Serializáció
	 * @param stream {@link ObjectOutputStream}
	 * @throws IOException 
	 */
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		
		 		stream.writeObject(entityType);
		 		stream.writeObject(sprite);
		 		
	}
	 
	 /**
	  * Deserializáció
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
	  * Beállitja a láthatóságot
	  * @param visibility : boolean - láthatóság
	  */
	public void setVisibility(boolean visibility) {
		sprite.setVisibility(visibility);
	}

	/**
	 * Beállitja a sprite pozicióját
	 * @param x : int - x pozició
	 * @param y : int - y pozició
	 */
	public void setLocation(int x, int y) {
		sprite.setLocation(x, y);
		
	}

	/**
	 * Visszatér a sprite X poziciójával
	 * @return x : int - x pozició
	 */
	public int GetPosX() {
		return sprite.GetPosX();
	}

	/**
	 * Visszatér a sprite Y poziciójával
	 * @return y : int - y pozició
	 */
	public int GetPosY() {
		return sprite.GetPosY();
	}

	/**
	 * Visszatér a sprite poziciójával
	 * @return point : {@link Point} - sprite pozició
	 */
	public Point GetLocation() {
		return sprite.GetLocation();
	}

	/**
	 * Visszatér a sprite rectangle magasságával
	 * @return spriteRectHeight : int - a sprite rectangle magassága
	 */
	public int GetHeight() {
		return sprite.getRectHeight();
	}

	/**
	 * Visszatér a sprite rectangljével
	 * @return spriteRect : {@link Rectangle} - a sprite rectanglje
	 */
	public Rectangle getRect() {
		return sprite.getRect();
	}
	
	/**
	 * Rectangle ütközést vizsgál
	 * @param rect : a vizsgálandó {@link Rectangle}
	 * @return true - ha van ütközés, false - ha nincs
	 */
	public boolean intersects(Rectangle rect){
		return sprite.getRect().intersects(rect);
	}

	/**
	 * Visszatér a sprite láthatóságával
	 * @return visibility : boolean - true ha látható , false ha nem
	 */
	public boolean isVisible() {
		return sprite.isVisible();
	}

	/**
	 * Visszatér a megadott direction alapján az adott oldal fal-e
	 * @param dir : {@link Dir} - a megadott direction
	 * @return boolean : true ha fal van a megadott direction alapján, false ha nincs
	 */
	public boolean getWall(Dir dir) {
		return	sprite.GetWall(dir);
	}

	/**
	 * Beállitja a sprite tipusát a megadott {@link SpriteType} alapján
	 * @param spriteType : {@link SpriteType}
	 */
	public void setSpriteType(SpriteType spriteType) {
		sprite.setSpriteType(spriteType);
	}
}
