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


public class DrawableEntity extends Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Sprite sprite;
	
	
	public DrawableEntity(String string, int x, int y, boolean visible) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(string,x,y,visible);
	}

	public DrawableEntity(String path, Rectangle rect, boolean visible) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(path,rect,visible);
	}

	public DrawableEntity(String path, Rectangle rect, boolean visible,
			boolean[] walls, SpriteType spriteType) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(path,rect,visible,walls,spriteType);
	}

	public void draw(Graphics g) {
		sprite.draw(g);
	}
	
	
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		
		 		stream.writeObject(entityType);
		 		stream.writeObject(sprite);
		 		
	}
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		 entityType = (EntityType)in.readObject();
		sprite = (Sprite)in.readObject();
		
	}
	 
	public void setVisibility(boolean visibility) {
		sprite.setVisibility(visibility);
	}

	public void setLocation(int x, int y) {
		sprite.setLocation(x, y);
		
	}

	public int GetPosX() {
		return sprite.GetPosX();
	}

	public int GetPosY() {
		return sprite.GetPosY();
	}

	public Point GetLocation() {
		return sprite.GetLocation();
	}

	public int GetHeight() {
		return sprite.getRectHeight();
	}

	public Rectangle getRect() {
		return sprite.getRect();
	}
	public boolean intersects(Rectangle rect){
		return sprite.getRect().intersects(rect);
	}

	public boolean isVisible() {
		return sprite.isVisible();
	}

	public boolean getWall(Dir dir) {
		return	sprite.GetWall(dir);
	}

	public void setSpriteType(SpriteType spriteType) {
		sprite.setSpriteType(spriteType);
	}
}
