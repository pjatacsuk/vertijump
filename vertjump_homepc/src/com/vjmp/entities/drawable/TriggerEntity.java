package com.vjmp.entities.drawable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.vjmp.entities.Entity.EntityType;
import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.Sprite.SpriteType;
/**
 * A {@link TriggerEntity} olyan entitás, amely amellet hogy ritkább esetben kirajzolandó, leginkább valamilyen
 * mellékhatással(pálya vége, elhalálozás,plusz pont,gyorsitás stb) rendelkezik. 
 * 
 *
 */
public abstract class TriggerEntity extends DrawableEntity {

	public enum TriggerType {FINISH_LINE,MESSAGE_BOX, SPIKE};
	
	protected boolean isActive;
	protected boolean isAlwaysVisible = false;
	private static final long serialVersionUID = 1L;
	
	protected TriggerType triggerType;
	
	/**
	 * Konstruktor
	 * @param string : {@link String} - a {@link TriggerEntity} sprite-jának elérési útvonala
	 * @param x : int - a {@link TriggerEntity} sprite-jának x poziciója
	 * @param y : int - a {@link TriggerEntity} sprite-jának y poziciója
	 * @param b : boolean - a {@link TriggerEntity} sprite-jának láthatósága
	 */
	public TriggerEntity(String string, int x, int y, boolean b) {
		super(string, x, y, b);
		entityType = EntityType.TRIGGER;
	}

	/**
	 * Konstruktor
	 * @param path : {@link String} - a {@link TriggerEntity} sprite-jának elérési útvonala
	 * @param rect : {@link Rectangle} - a {@link TriggerEntity} sprite-jának {@link Rectangle}-je
	 * @param b : a {@link TriggerEntity} sprite-jának láthatósága
	 */
	public TriggerEntity(String path, Rectangle rect, boolean b) {
		super(path, rect,b);
		entityType = EntityType.TRIGGER;
	}
	/**
	 * Konstruktor
	 * @param path : a {@link TriggerEntity} sprite-jának elérési útvonala
	 * @param rect : a {@link TriggerEntity} sprite-jának {@link Rectangle}-je
	 * @param b : boolean - a {@link TriggerEntity} sprite-jának láthatósága
	 * @param walls : boolean[] -  a {@link TriggerEntity} sprite-jának
	 * @param spriteType : {@link SpriteType} - a {@link TriggerEntity} sprite-jának tipusához
	 */
	public TriggerEntity(String path, Rectangle rect, boolean b, boolean[] walls,SpriteType spriteType) {
		super(path,rect,b,walls,spriteType);
		entityType = EntityType.TRIGGER;
	}

	/**
	 * Visszatér a trigger tipusával
	 * @return triggerType : {@link TriggerType} - a trigger tipusa
	 */
	public TriggerType getTriggerType() {
		return triggerType;
	}
	
	/**
	 * Aktiválja a triggert.
	 */
	public void activateTrigger() {
			isActive = true;
	}
	
	/**
	 * "Disaktiválja" a triggert
	 */
	public void disactivateTrigger() {
			isActive = false;
	}
	
	/**
	 * Visszatér a trigger aktivitásával
	 * @return true - ha aktiv a trigger, false ha nem
	 */
	public boolean getActivity() {
		return isActive;
	}
	
	/**
	 * Serializáció
	 * @param stream : {@link ObjectOutputStream}
 	 * @throws IOException
	 */
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
	 /**
	  * Deserializáció
	  * @param in : {@link ObjectInputStream}
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
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
	 /**
	  * Override-olandó kirajzolás függvény 
	  */
	@Override
	public void draw(Graphics g) {
		
	}
	
	/**
	 * Default kirajzoló függvény, amely akkor rajzolja ki {@link TriggerEntity}-hez tartozó sprite-ot
	 * ha editorban vagyunk. Illetve akkor hajta végre a draw(Graphics g) függvényt, ha aktiv vagy mindig látható
	 * @param g
	 * @param isEditor
	 */
	public void draw(Graphics g,boolean isEditor) {
		if(isEditor) {	
			sprite.draw(g);
			draw(g);
		} else if(isActive || isAlwaysVisible) {
			draw(g);
		}
	}

	/**
	 * Override-olandó update függvény
	 */
	public void update() {
		
	}
	 

}
