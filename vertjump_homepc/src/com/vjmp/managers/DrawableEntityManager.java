package com.vjmp.managers;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.vjmp.entities.drawable.DrawableEntity;

public class DrawableEntityManager extends EntityManager<DrawableEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DrawableEntityManager() {
		super();
		
	}

	public DrawableEntityManager(DrawableEntityManager entityManager) {
		super(entityManager);
	}

	public void DrawSprites(Graphics g) {
		for(DrawableEntity drawable : this.list) {
			
			drawable.draw(g);
		}
		
	}

	public void remove(Rectangle rect) {
		for(int i=0;i<size();i++) {
			if(list.get(i).getRect().intersects(rect)) {
				list.remove(i);
			}
		}
		
	}
	
	
	
}
