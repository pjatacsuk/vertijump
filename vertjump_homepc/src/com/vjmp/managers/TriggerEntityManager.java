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

/**
 * A {@link TriggerEntityManager} a {@link TriggerEntity}-k kezelését végzi.
 * Frissiti,kirajzolja, aktiválja,deaktiválja stb... ezeket.
 * 
 *
 */
public class TriggerEntityManager extends EntityManager<TriggerEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean isEditor = false;
	
	/**
	 * Konstruktor
	 * @param editor : boolean - true - ha editorhoz tartozik, false - ha nem
	 */
	public TriggerEntityManager(boolean editor) {
		super();
		isEditor = editor;
	}

	/**
	 * Másoló Konstruktor
	 * @param entityManager : {@link TriggerEntityManager} 
	 * @param isEditor : boolean - true - ha editorhoz tartozik, false - ha nem
	 */
	public TriggerEntityManager(TriggerEntityManager entityManager,boolean isEditor) {
		super(entityManager);
		isEditor = entityManager.isEditor;
	}

	/**
	 * Kirajzolja az adott {@link TriggerEntity}-ket.
	 * @param g : {@link Graphics}
	 */
	public synchronized void DrawSprites(Graphics g) {
		for(TriggerEntity trigger : this.list) {
		
				trigger.draw(g,isEditor);
		
		}
		
	}

	/**
	 * Kitörli azokat a triggereket, amelyeket lefed az adott {@link Rectangle}.
	 * @param rect : {@link Rectangle} - az adott {@link Rectangle}.
	 */
	public synchronized void remove(Rectangle rect) {
		for(int i=0;i<size();i++) {
			if(list.get(i).getRect().intersects(rect)) {
				list.remove(i);
			}
		}
		
	}
	
	/**
	 * Serializáció
	 * 
	 * @param stream : {@link ObjectOutputStream}
	 * @throws IOException
	 */
	private synchronized void writeObject(ObjectOutputStream stream) 
	        throws IOException { 
		 stream.writeInt(list.size());
		 stream.writeBoolean(isEditor);
		 for(TriggerEntity entity : list) {
			stream.writeObject(entity);
		 }
		 
	}
	
	/**
	 * Deserializáció
	 * @param in : {@link ObjectInputStream}
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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

	/**
	 * Updateli a listát.
	 * @param camera : {@link Camera}
	 */
	public void update(Camera camera) {
		for(TriggerEntity triggerEntity : list){
			triggerEntity.update();
		}
		
	}
	
	
}