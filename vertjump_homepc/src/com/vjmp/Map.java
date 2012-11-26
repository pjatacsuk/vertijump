package com.vjmp;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

import com.vjmp.entities.Entity.EntityType;
import com.vjmp.entities.drawable.DrawableEntity;
import com.vjmp.entities.drawable.TriggerEntity;
import com.vjmp.gfx.Camera;
import com.vjmp.managers.DrawableEntityManager;
import com.vjmp.managers.TriggerEntityManager;
/**
 * 
 *Az oszt�ly v�gzi a p�lya megjelenit�s�t, updateles�t� mind az editor mind pedig a j�t�k folyam�n.
 *Implement�lja az {@link Iterable} interface-t (DrawableEnity param�terrel), {@link Serializable}.
 */
public class Map implements Iterable<DrawableEntity>,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private	int	WIDTH;
	private int HEIGHT;
	
	private DrawableEntityManager entityManager = null;
	private TriggerEntityManager triggerEntityManager = null;
	private DrawableEntityManager visibleEntities = null;
	private DrawableEntityManager notVisibleEntities = null;
	
	public boolean isEditor = false;
	private boolean running = true;
	
	private Camera camera = null;

	/**
	 * Konstruktor 
	 * @param width : int - Az ablak sz�less�ge
 	 * @param height : int - Az ablak magass�ga
	 * @param iseditor : boolean - true ha editornak a mapja,false ha a j�t�k mapja
	 */
	public Map(int width,int height,boolean iseditor) {
		
		WIDTH = width;
		HEIGHT= height;
		isEditor = iseditor;

		entityManager = new DrawableEntityManager();
		triggerEntityManager = new TriggerEntityManager(iseditor);
		visibleEntities = new DrawableEntityManager();
		notVisibleEntities = new DrawableEntityManager();
		entityManager.add(new DrawableEntity("./res/debug_platform.png",0,HEIGHT-25,true));
		loadNotVisibleEntites();
	}
	
	/**
	 * Copy konstruktor
	 * @param map : {@link Map}
	 * @param isEditor : boolean - true ha editornak a mapja, false ha a j�t�k mapja
	 */
	public Map(Map map,boolean isEditor) {
		
		WIDTH = map.WIDTH;
		HEIGHT = map.HEIGHT;
		this.isEditor = isEditor;
		entityManager = new DrawableEntityManager(map.entityManager);
		triggerEntityManager = new TriggerEntityManager(map.triggerEntityManager,false);
		visibleEntities = new DrawableEntityManager(map.visibleEntities);
		notVisibleEntities = new DrawableEntityManager(map.notVisibleEntities);
		loadNotVisibleEntites();
		
	}
	
	/**
	 * A map megjelenit�s�t v�gzi
	 * @param g : {@link Graphics} 
	 */
	public void draw(Graphics g) {
		//csak a l�that� entit�sokat, illetve triggereket kell kirajzolni
		visibleEntities.DrawSprites(g);
		triggerEntityManager.DrawSprites(g);
	}


	
	@Override
	public Iterator<DrawableEntity> iterator() {
		return entityManager.iterator();
	}
	
	/**
	 * Updateli a map-et a camera f�ggv�ny�ben �s a hovatartoz�s(editor,game) f�ggv�ny�ben
	 * @param camera : {@link Camera}
	 */
	public synchronized void update(Camera camera) {
		
		if(!isEditor)	{
			UpdateSprites(camera.pos_y);
		}	else {
			UpdateEditorSprites(camera.pos_y);
		}
		triggerEntityManager.update(camera);
		
	}
	

	/**
	 * Updateli az Editor k�vetelm�nyeinek megfelel�en (nincs t�rl�s), a map-et
	 * @param pos_y : int - A camera y pozici�ja
	 */
	private void UpdateEditorSprites(int pos_y) {
		UpdateNotVisibleSprites(pos_y);
		
	}
	
	/**
	 * Felt�lti a nem l�thatj� entit�sok list�j�t a f� entit�s list�b�l.
	 * 
	 */
	public void loadNotVisibleEntites() {
		for(int i=0;i<entityManager.size();i++){
			notVisibleEntities.add(entityManager.get(i));
		}
	}
	
	/**
	 * Updateli a nem l�that� sprite-ok list�j�t, ha l�that� k�pbe ker�l egy sprite, bedobja a l�that�
	 * sprite-ok list�j�ba, �s t�rli a nem l�that�k k�z�l
	 * @param CameraY : int - A camera Y pozici�ja
	 */
	public void UpdateNotVisibleSprites(int CameraY) {
		for(int i=0;i<notVisibleEntities.size();i++) {
			DrawableEntity tmp = notVisibleEntities.get(i);
			
			//ha az entit�s a l�that� k�ptartom�nyba van, l�that�v� tessz�k
			if(tmp.GetPosY() + tmp.getRect().height > -CameraY) {
				tmp.setVisibility(true);
				visibleEntities.add(tmp);
				notVisibleEntities.remove(i);
			}
		}
	}
	
	/**
	 * Updateli a l�that� sprite-ok list�j�t, ha a l�that� sprite kiker�l a l�that� k�pt�rb�l, t�rli a list�b�l.
	 * @param CameraY : int - A camera Z pozici�ja
	 */
	public void UpdateVisibleSprites(int CameraY) {
		for(int i=0;i<visibleEntities.size();i++){
			DrawableEntity tmp = visibleEntities.get(i);
			if(tmp.GetPosY() > -CameraY + HEIGHT){
				visibleEntities.remove(i);
			}
		}
	}
	
	/** 
	 * Updateli a sprite-ok list�j�t (j�t�knak megfelel�en: t�rli a m�r nem l�that� sprite-okat)
	 * @param CameraY : int - a Camera pozici�ja
	 */
	public void UpdateSprites(int CameraY) {
		UpdateNotVisibleSprites(CameraY);
		UpdateVisibleSprites(CameraY);
	}
	
	/**
	 * 
	 * @return triggerEntityManager : {@link TriggerEntityManager}
	 */
	public TriggerEntityManager getTriggerEntityManager() {
		return triggerEntityManager;
	}
	
	/**
	 * Hozz�adja a map-hez a sprite-ot, a l�that�s�g�nak f�ggv�ny�ben
	 * @param sprite : {@link DrawableEntity}
	 */
	public void add(DrawableEntity sprite) {
		if(sprite.getType() == EntityType.TRIGGER) {
			triggerEntityManager.add((TriggerEntity)sprite);
		} else {
			//entityManager-be a ment�s miatt mindenk�pp berakjuk
			entityManager.add(sprite);
			if(sprite.isVisible()) {
				visibleEntities.add(sprite);
			} else {
				notVisibleEntities.add(sprite);
			}
		}
	}
	
	/**
	 * Serializ�ci�
	 * @param stream : {@link ObjectOutputStream}
	 * @throws IOException
	 */
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		stream.writeInt(WIDTH);
		stream.writeInt(HEIGHT);
		stream.writeBoolean(isEditor);
		 stream.writeObject(entityManager);
		 stream.writeObject(triggerEntityManager);
	 }
	 
	 /**
	  * Deserializ�ci�
	  * @param in : {@link ObjectInputStream}
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		
		 System.out.println("map_read");
		 WIDTH = in.readInt();
		 HEIGHT = in.readInt();
		 isEditor = in.readBoolean();
		 entityManager = (DrawableEntityManager)in.readObject();
		 triggerEntityManager = (TriggerEntityManager)in.readObject();
		 visibleEntities = new DrawableEntityManager();
		notVisibleEntities = new DrawableEntityManager();
		loadNotVisibleEntites();
		System.out.println("COmpltete");
	 }
	 
	/**
	 *  A rectangle alapj�n t�rli az entit�sokat
	 * @param rect : {@link Rectangle}
	 */
	public void remove(Rectangle rect) {
		visibleEntities.remove(rect);
		entityManager.remove(rect);
		triggerEntityManager.remove(rect);
	}
	
	/**
	 * Be�llitja a kamer�t
	 * @param camera : {@link Camera}
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	/**
	 * V�gig iter�lunk az entit�sokon �s l�that�s�gukat false-ra �llitjuk
	 */
	public void setVisibilityFalse() {
		for(DrawableEntity drawableEntity : entityManager) {
			drawableEntity.setVisibility(false);
		}
	}
	
	/**
	 * 
	 * @param i : int - Index
	 * @return i-edik {@link DrawableEntity}
	 */
	public DrawableEntity getVisibleEntity(int i) {
		return visibleEntities.get(i);
	}
	
	/**
	 * 
	 * @return size : int - A l�that� entit�s lista m�ret�t adja meg
	 */
	public int getVisibleEntitiesSize() {
		return visibleEntities.size();
	}
	
	/**
	 * 
	 * @return drawableEntityManager : {@link DrawableEntityManager} - A l�that� entit�sok list�j�t adja meg 
	 */
	public DrawableEntityManager getVisibleEntityManager() {
		return visibleEntities;
	}
	
	
}
