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
 * Serializ�lhat� List-a elrendez�st haszn�l� alap Entity Mmanager.
 * 
 * @param <T> : a t�roland� elem tipusa
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
	 * M�sol� konstruktor
	 * @param spriteManager
	 */
	public EntityManager(EntityManager spriteManager) {
		list = new ArrayList<T>(spriteManager.list);
	}

	/**
	 * Hozz�ad egy entit�st a list�hoz
	 * @param entity : T - a hozz�adand� entit�s
	 */
	public synchronized void add(T entity) {
		list.add(entity);
	}
	
	/**
	 * Visszat�r az index �ltal meghat�rozott bejegyz�ssel
	 * @param i : int - index
	 * @return ret : T - az index �ltal meghat�rozott bejegyz�s
	 */
	public  synchronized T get(int i) {
		return list.get(i);
		
	}
	
	/**
	 * Kit�rli az i index �ltal meghat�rozott bejegyz�st
	 * @param i : int - index
	 */
	public  synchronized void remove(int i) {
		list.remove(i);
	}
	
	/**
	 * Pop-olja az i �ltal meghat�rozott bejegyz�st
	 * @param i : int - index
	 * @return ret : T - az i �ltal meghat�rozott bejegyz�s
	 */
	public  synchronized T pop(int i) {
		T ret = list.get(i);
		list.remove(i);
		return ret;
	
	}
	
	
	/**
	 * Visszat�r a list�ban utols� bejegyz�ssel.
	 * @return ret : T - a lista utols� bejegyz�se
	 */
	public  synchronized T getLast() {
		if(list.isEmpty()) return null;
		return list.get(list.size()-1);
	}
	
	/**
	 * Visszat�r a lista m�ret�vel
	 * @return size : int - a lista m�rete
	 */
	public  synchronized int size() {
		return list.size();
	}

	@Override
	public  synchronized Iterator<T> iterator() {
		return list.iterator();
	}
	
	/**
	 * Serializ�ci�
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
	  * Deserializ�ci�
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
