package com.vjmp.managers;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ListIterator;

import com.vjmp.entities.drawable.DrawableEntity;

/**
 * A class olyan DrawableEntity-ket t�rol, frissit, kirajzol. 
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
	 * M�sol� Konstruktor
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
	 * Kit�rli a rectangle �ltal lefedett {@link DrawableEntity}-ket
	 * @param rect : {@link Rectangle}
	 */
	public synchronized void remove(Rectangle rect) {
		ListIterator<DrawableEntity> it = list.listIterator();
		while(it.hasNext()){
			DrawableEntity tmp = it.next();
			if(tmp.getRect().intersects(rect)){
				it.remove();
			}
		}
		
	}
	
	
	
}
