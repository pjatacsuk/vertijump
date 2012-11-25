package com.vjmp.managers;

import com.vjmp.entities.Entity;
import com.vjmp.entities.drawable.DrawableEntity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Serializálható List-a elrendezést használó alap Entity Mmanager.
 * 
 * @param <T> : a tárolandó elem tipusa
 */
public class EntityManager<T> implements Iterable<T>,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<T> list;
	
	/**
	 * Konstruktor
	 */
	public EntityManager() {
		list = new ArrayList<T>();
	}
	
	/**
	 * Másoló konstruktor
	 * @param spriteManager
	 */
	public EntityManager(EntityManager spriteManager) {
		list = new ArrayList<T>(spriteManager.list);
	}

	/**
	 * Hozzáad egy entitást a listához
	 * @param entity : T - a hozzáadandó entitás
	 */
	public synchronized void add(T entity) {
		list.add(entity);
	}
	
	/**
	 * Visszatér az index által meghatározott bejegyzéssel
	 * @param i : int - index
	 * @return ret : T - az index által meghatározott bejegyzés
	 */
	public  synchronized T get(int i) {
		return list.get(i);
		
	}
	
	/**
	 * Kitörli az i index által meghatározott bejegyzést
	 * @param i : int - index
	 */
	public  synchronized void remove(int i) {
		list.remove(i);
	}
	
	/**
	 * Pop-olja az i által meghatározott bejegyzést
	 * @param i : int - index
	 * @return ret : T - az i által meghatározott bejegyzés
	 */
	public  synchronized T pop(int i) {
		T ret = list.get(i);
		list.remove(i);
		return ret;
	
	}
	
	
	/**
	 * Visszatér a listában utolsó bejegyzéssel.
	 * @return ret : T - a lista utolsó bejegyzése
	 */
	public  synchronized T getLast() {
		if(list.isEmpty()) return null;
		return list.get(list.size()-1);
	}
	
	/**
	 * Visszatér a lista méretével
	 * @return size : int - a lista mérete
	 */
	public  synchronized int size() {
		return list.size();
	}

	@Override
	public  synchronized Iterator<T> iterator() {
		return list.iterator();
	}
	
	/**
	 * Serializáció
	 * @param stream : {@link ObjectOutputStream}
	 * @throws IOException
	 */
	 private  synchronized void writeObject(ObjectOutputStream stream)
		        throws IOException {
		 stream.writeInt(list.size());
		 for(T entity : list) {
			stream.writeObject(entity);
		 }
		 
		 
	 }
	 
	 /**
	  * Deserializáció
	  * @param in : {@link ObjectInputStream}
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 private  synchronized void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		 
		System.out.println("spriteManagaer");
		list = new ArrayList<T>();
		int size = in.readInt();
		for(int i=0;i<size;i++) {
			list.add((T)in.readObject());
		
		}
	 }

	
	
}
