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


public class DrawableEntity extends Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Sprite sprite;
	
	
	public DrawableEntity(String string, int x, int y, boolean b) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(string,x,y,b);
	}

	public DrawableEntity(String path, Rectangle rect, boolean b) {
		entityType = entityType.BLOCK;
		sprite = new Sprite(path,rect,b);
	}

	public void draw(Graphics g) {
		sprite.draw(g);
	}
	/*a
	public EntityType getType() {
		return super.getType();
	}*/
	
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
		return sprite.GetHeight();
	}

	public Rectangle getRect() {
		return sprite.getRect();
	}

	public boolean isVisible() {
		return sprite.isVisible();
	}
}
