package com.vjmp.managers;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.vjmp.entities.drawable.DrawableEntity;
import com.vjmp.entities.drawable.TriggerEntity;
import com.vjmp.gfx.Camera;

public class TriggerEntityManager extends EntityManager<TriggerEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean isEditor = false;
	
	public TriggerEntityManager(boolean editor) {
		super();
		isEditor = editor;
		int CameraY = 0;
	}

	public TriggerEntityManager(TriggerEntityManager entityManager,boolean isEditor) {
		super(entityManager);
		isEditor = entityManager.isEditor;
	}

	public synchronized void DrawSprites(Graphics g) {
		for(TriggerEntity trigger : this.list) {
		
				trigger.draw(g,isEditor);
		
		}
		
	}

	public synchronized void remove(Rectangle rect) {
		for(int i=0;i<size();i++) {
			if(list.get(i).getRect().intersects(rect)) {
				list.remove(i);
			}
		}
		
	}
	
	private synchronized void writeObject(ObjectOutputStream stream) 
	        throws IOException { 
		 stream.writeInt(list.size());
		 stream.writeBoolean(isEditor);
		 for(TriggerEntity entity : list) {
			stream.writeObject(entity);
		 }
		 
	}
	
	private synchronized void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		 
		System.out.println("triggerEntityManagaer");
		list = new ArrayList<TriggerEntity>();
		int size = in.readInt();
		isEditor = in.readBoolean();
		for(int i=0;i<size;i++) {
			list.add((TriggerEntity)in.readObject());
		
		}
		
	}

	public void update(Camera camera) {
		
		
	}
	
	
}