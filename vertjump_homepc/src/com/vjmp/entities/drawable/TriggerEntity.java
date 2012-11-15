package com.vjmp.entities.drawable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.vjmp.entities.Entity.EntityType;
import com.vjmp.gfx.Sprite;

public abstract class TriggerEntity extends DrawableEntity {

	public enum TriggerType {FINISH_LINE,MESSAGE_BOX, SPIKE};
	
	/**
	 * 
	 */
	
	protected boolean isActive;
	protected boolean isAlwaysVisible = false;
	private static final long serialVersionUID = 1L;
	
	protected TriggerType triggerType;
	
	public TriggerEntity(String string, int x, int y, boolean b) {
		super(string, x, y, b);
		entityType = EntityType.TRIGGER;
	}

	public TriggerEntity(String path, Rectangle rect, boolean b) {
		super(path, rect,b);
		entityType = EntityType.TRIGGER;
	}
	
	public TriggerEntity(String path, Rectangle rect, boolean b, boolean[] walls) {
		super(path,rect,b,walls);
		entityType = EntityType.TRIGGER;
	}

	public TriggerType getTriggerType() {
		return triggerType;
	}
	public void activateTrigger() {
			isActive = true;
	}
	public void disactivateTrigger() {
			isActive = false;
	}
	public boolean getActivity() {
		return isActive;
	}
	
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		 		
		 		//general Trigger types
		 		stream.writeBoolean(isActive);
		 		stream.writeBoolean(isAlwaysVisible);
		 		stream.writeObject(entityType);
		 		stream.writeObject(triggerType);
		 		
		 		//trigger sprite for editor
		 		stream.writeObject(sprite);
		 		
	}
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		//general Trigger types
		 isActive = in.readBoolean();
		 isAlwaysVisible = in.readBoolean();
		 entityType = (EntityType)in.readObject();
		 triggerType = (TriggerType)in.readObject();
		 
		//trigger sprite for editor
		 sprite = (Sprite)in.readObject();
		 
		
	}
	@Override
	public void draw(Graphics g) {
		
	}
	public void draw(Graphics g,boolean isEditor) {
		if(isEditor) {	
			sprite.draw(g);
			draw(g);
		} else if(isActive || isAlwaysVisible) {
			draw(g);
		}
	}

	public void update() {
		
	}
	 

}
