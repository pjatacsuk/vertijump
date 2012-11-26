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
 *Az osztály végzi a pálya megjelenitését, updatelesété mind az editor mind pedig a játék folyamán.
 *Implementálja az {@link Iterable} interface-t (DrawableEnity paraméterrel), {@link Serializable}.
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
	 * @param width : int - Az ablak szélessége
 	 * @param height : int - Az ablak magassága
	 * @param iseditor : boolean - true ha editornak a mapja,false ha a játék mapja
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
	 * @param isEditor : boolean - true ha editornak a mapja, false ha a játék mapja
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
	 * A map megjelenitését végzi
	 * @param g : {@link Graphics} 
	 */
	public void draw(Graphics g) {
		//csak a látható entitásokat, illetve triggereket kell kirajzolni
		visibleEntities.DrawSprites(g);
		triggerEntityManager.DrawSprites(g);
	}


	
	@Override
	public Iterator<DrawableEntity> iterator() {
		return entityManager.iterator();
	}
	
	/**
	 * Updateli a map-et a camera függvényében és a hovatartozás(editor,game) függvényében
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
	 * Updateli az Editor követelményeinek megfelelõen (nincs törlés), a map-et
	 * @param pos_y : int - A camera y poziciója
	 */
	private void UpdateEditorSprites(int pos_y) {
		UpdateNotVisibleSprites(pos_y);
		
	}
	
	/**
	 * Feltölti a nem láthatjó entitások listáját a fõ entitás listából.
	 * 
	 */
	public void loadNotVisibleEntites() {
		for(int i=0;i<entityManager.size();i++){
			notVisibleEntities.add(entityManager.get(i));
		}
	}
	
	/**
	 * Updateli a nem látható sprite-ok listáját, ha látható képbe kerül egy sprite, bedobja a látható
	 * sprite-ok listájába, és törli a nem láthatók közül
	 * @param CameraY : int - A camera Y poziciója
	 */
	public void UpdateNotVisibleSprites(int CameraY) {
		for(int i=0;i<notVisibleEntities.size();i++) {
			DrawableEntity tmp = notVisibleEntities.get(i);
			
			//ha az entitás a látható képtartományba van, láthatóvá tesszük
			if(tmp.GetPosY() + tmp.getRect().height > -CameraY) {
				tmp.setVisibility(true);
				visibleEntities.add(tmp);
				notVisibleEntities.remove(i);
			}
		}
	}
	
	/**
	 * Updateli a látható sprite-ok listáját, ha a látható sprite kikerül a látható képtérbõl, törli a listából.
	 * @param CameraY : int - A camera Z poziciója
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
	 * Updateli a sprite-ok listáját (játéknak megfelelõen: törli a már nem látható sprite-okat)
	 * @param CameraY : int - a Camera poziciója
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
	 * Hozzáadja a map-hez a sprite-ot, a láthatóságának függvényében
	 * @param sprite : {@link DrawableEntity}
	 */
	public void add(DrawableEntity sprite) {
		if(sprite.getType() == EntityType.TRIGGER) {
			triggerEntityManager.add((TriggerEntity)sprite);
		} else {
			//entityManager-be a mentés miatt mindenképp berakjuk
			entityManager.add(sprite);
			if(sprite.isVisible()) {
				visibleEntities.add(sprite);
			} else {
				notVisibleEntities.add(sprite);
			}
		}
	}
	
	/**
	 * Serializáció
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
	  * Deserializáció
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
	 *  A rectangle alapján törli az entitásokat
	 * @param rect : {@link Rectangle}
	 */
	public void remove(Rectangle rect) {
		visibleEntities.remove(rect);
		entityManager.remove(rect);
		triggerEntityManager.remove(rect);
	}
	
	/**
	 * Beállitja a kamerát
	 * @param camera : {@link Camera}
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	/**
	 * Végig iterálunk az entitásokon és láthatóságukat false-ra állitjuk
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
	 * @return size : int - A látható entitás lista méretét adja meg
	 */
	public int getVisibleEntitiesSize() {
		return visibleEntities.size();
	}
	
	/**
	 * 
	 * @return drawableEntityManager : {@link DrawableEntityManager} - A látható entitások listáját adja meg 
	 */
	public DrawableEntityManager getVisibleEntityManager() {
		return visibleEntities;
	}
	
	
}
