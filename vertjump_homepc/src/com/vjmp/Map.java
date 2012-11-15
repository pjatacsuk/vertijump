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

public class Map implements Iterable<DrawableEntity>,Serializable,Runnable {
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
	
/*	public Map(int width,int height) {
		entityManager = new DrawableEntityManager();
		triggerEntityManager = new TriggerEntityManager(false);
		WIDTH = width;
		HEIGHT= height;
		visibleEntities = new DrawableEntityManager();
		notVisibleEntities = new DrawableEntityManager();
	//	GenerateMap(100);
		GenerateTest2(120);
		entityManager.add(new DrawableEntity("./res/debug_platform.png",0,HEIGHT-25,true));
	}*/
	
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
	public void draw(Graphics g) {
		visibleEntities.DrawSprites(g);
		triggerEntityManager.DrawSprites(g);
	}


	
	@Override
	public Iterator<DrawableEntity> iterator() {
		return entityManager.iterator();
	}
	public synchronized void update(Camera camera) {
		
		if(!isEditor)	{
			UpdateSprites(camera.pos_y);
		}	else {
			UpdateEditorSprites(camera.pos_y);
		}
		triggerEntityManager.update(camera);
		
}
	


	private void UpdateEditorSprites(int pos_y) {
		UpdateNotVisibleSprites(pos_y);
		
	}
	public void loadNotVisibleEntites() {
		for(int i=0;i<entityManager.size();i++){
			notVisibleEntities.add(entityManager.get(i));
		}
	}
	public void UpdateNotVisibleSprites(int CameraY) {
		for(int i=0;i<notVisibleEntities.size();i++) {
			DrawableEntity tmp =notVisibleEntities.get(i);
			if(tmp.GetPosY() + tmp.getRect().height > -CameraY) {
				tmp.setVisibility(true);
				visibleEntities.add(tmp);
				notVisibleEntities.remove(i);
			}
		}
	}
	public void UpdateVisibleSprites(int CameraY) {
		for(int i=0;i<visibleEntities.size();i++){
			DrawableEntity tmp = visibleEntities.get(i);
			if(tmp.GetPosY() > -CameraY + HEIGHT){
				visibleEntities.remove(i);
			}
		}
	}
	public void UpdateSprites(int CameraY) {
		UpdateNotVisibleSprites(CameraY);
		UpdateVisibleSprites(CameraY);
	}
	public TriggerEntityManager getTriggerEntityManager() {
		return triggerEntityManager;
	}
	
	public void add(DrawableEntity sprite) {
		if(sprite.getType() == EntityType.TRIGGER) {
		triggerEntityManager.add((TriggerEntity)sprite);
		} else {
			entityManager.add(sprite);
			if(sprite.isVisible()) {
				visibleEntities.add(sprite);
			} else {
				notVisibleEntities.add(sprite);
			}
		}
	}
	
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		stream.writeInt(WIDTH);
		stream.writeInt(HEIGHT);
		stream.writeBoolean(isEditor);
		 stream.writeObject(entityManager);
		 stream.writeObject(triggerEntityManager);
		
		 
	 }
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
	public void remove(Rectangle rect) {
		visibleEntities.remove(rect);
		entityManager.remove(rect);
		triggerEntityManager.remove(rect);
	}
	@Override
	public void run() {
		while(running) {
			try {
				update(camera);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void notifyThread() {
	
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	public void setVisibilityFalse() {
		for(DrawableEntity drawableEntity : entityManager) {
			drawableEntity.setVisibility(false);
			System.out.println("valami");
		}
	}
	public DrawableEntity getVisibleEntity(int i) {
		return visibleEntities.get(i);
	}
	public int getVisibleEntitiesSize() {
		return visibleEntities.size();
	}
	public DrawableEntityManager getVisibleEntityManager() {
		return visibleEntities;
	}
	
	
}
