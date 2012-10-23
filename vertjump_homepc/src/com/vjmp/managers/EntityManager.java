package com.vjmp.managers;

import com.vjmp.entities.Entity;
import com.vjmp.entities.drawable.DrawableEntity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityManager<T> implements Iterable<T>,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<T> list;
	
	public EntityManager() {
		list = new ArrayList<T>();
	}
	
	public EntityManager(EntityManager spriteManager) {
		list = new ArrayList<T>(spriteManager.list);
	}

	public void add(T entity) {
		list.add(entity);
	}
	public T get(int i) {
		return list.get(i);
		
	}
	
	public void remove(int i) {
		list.remove(i);
	}
	public T pop(int i) {
		T ret = list.get(i);
		list.remove(i);
		return ret;
	
	}
	
	

	public T getLast() {
		if(list.isEmpty()) return null;
		return list.get(list.size()-1);
	}
	public int size() {
		return list.size();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		 stream.writeInt(list.size());
		 for(T entity : list) {
			stream.writeObject(entity);
		 }
		 
		 
	 }
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		 
		System.out.println("spriteManagaer");
		list = new ArrayList<T>();
		int size = in.readInt();
		for(int i=0;i<size;i++) {
			list.add((T)in.readObject());
		
		}
		
		
		
		
	 }

	/*public void remove(Rectangle rect) {
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getRect().intersects(rect)) {
				list.remove(i);
			}
		}
		
	}*/
	
}
