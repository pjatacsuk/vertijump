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
 * A {@link TriggerEntity} olyan entit�s, amely amellet hogy ritk�bb esetben kirajzoland�, legink�bb valamilyen
 * mell�khat�ssal(p�lya v�ge, elhal�loz�s,plusz pont,gyorsit�s stb) rendelkezik. 
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
	 * @param string : {@link String} - a {@link TriggerEntity} sprite-j�nak el�r�si �tvonala
	 * @param x : int - a {@link TriggerEntity} sprite-j�nak x pozici�ja
	 * @param y : int - a {@link TriggerEntity} sprite-j�nak y pozici�ja
	 * @param b : boolean - a {@link TriggerEntity} sprite-j�nak l�that�s�ga
	 */
	public TriggerEntity(String string, int x, int y, boolean b) {
		super(string, x, y, b);
		entityType = EntityType.TRIGGER;
	}

	/**
	 * Konstruktor
	 * @param path : {@link String} - a {@link TriggerEntity} sprite-j�nak el�r�si �tvonala
	 * @param rect : {@link Rectangle} - a {@link TriggerEntity} sprite-j�nak {@link Rectangle}-je
	 * @param b : a {@link TriggerEntity} sprite-j�nak l�that�s�ga
	 */
	public TriggerEntity(String path, Rectangle rect, boolean b) {
		super(path, rect,b);
		entityType = EntityType.TRIGGER;
	}
	/**
	 * Konstruktor
	 * @param path : a {@link TriggerEntity} sprite-j�nak el�r�si �tvonala
	 * @param rect : a {@link TriggerEntity} sprite-j�nak {@link Rectangle}-je
	 * @param b : boolean - a {@link TriggerEntity} sprite-j�nak l�that�s�ga
	 * @param walls : boolean[] -  a {@link TriggerEntity} sprite-j�nak
	 * @param spriteType : {@link SpriteType} - a {@link TriggerEntity} sprite-j�nak tipus�hoz
	 */
	public TriggerEntity(String path, Rectangle rect, boolean b, boolean[] walls,SpriteType spriteType) {
		super(path,rect,b,walls,spriteType);
		entityType = EntityType.TRIGGER;
	}

	/**
	 * Visszat�r a trigger tipus�val
	 * @return triggerType : {@link TriggerType} - a trigger tipusa
	 */
	public TriggerType getTriggerType() {
		return triggerType;
	}
	
	/**
	 * Aktiv�lja a triggert.
	 */
	public void activateTrigger() {
			isActive = true;
	}
	
	/**
	 * "Disaktiv�lja" a triggert
	 */
	public void disactivateTrigger() {
			isActive = false;
	}
	
	/**
	 * Visszat�r a trigger aktivit�s�val
	 * @return true - ha aktiv a trigger, false ha nem
	 */
	public boolean getActivity() {
		return isActive;
	}
	
	/**
	 * Serializ�ci�
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
	  * Deserializ�ci�
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
	  * Override-oland� kirajzol�s f�ggv�ny 
	  */
	@Override
	public void draw(Graphics g) {
		
	}
	
	/**
	 * Default kirajzol� f�ggv�ny, amely akkor rajzolja ki {@link TriggerEntity}-hez tartoz� sprite-ot
	 * ha editorban vagyunk. Illetve akkor hajta v�gre a draw(Graphics g) f�ggv�nyt, ha aktiv vagy mindig l�that�
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
	 * Override-oland� update f�ggv�ny
	 */
	public void update() {
		
	}
	 

}
