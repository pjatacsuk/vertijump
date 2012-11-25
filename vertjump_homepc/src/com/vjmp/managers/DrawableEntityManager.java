package com.vjmp.managers;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.vjmp.entities.drawable.DrawableEntity;

/**
 * A class olyan DrawableEntity-ket tárol, frissit, kirajzol. 
 * 
 *
 */

public class DrawableEntityManager extends EntityManager<DrawableEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Konstruktor
	 */
	public DrawableEntityManager() {
		super();
		
	}

	/**
	 * Másoló Konstruktor
	 * @param entityManager
	 */
	public DrawableEntityManager(DrawableEntityManager entityManager) {
		super(entityManager);
	}

	/**
	 * Kirajzolja a {@link DrawableEntity}-ket
	 * @param g : {@link Graphics}
	 */
	public synchronized void DrawSprites(Graphics g) {
		for(DrawableEntity drawable : this.list) {
			
			drawable.draw(g);
		}
		
	}

	/**
	 * Kitörli a rectangle által lefedett {@link DrawableEntity}-ket
	 * @param rect : {@link Rectangle}
	 */
	public synchronized void remove(Rectangle rect) {
		for(int i=0;i<size();i++) {
			if(list.get(i).getRect().intersects(rect)) {
				list.remove(i);
			}
		}
		
	}
	
	
	
}
