package com.vjmp.managers;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.vjmp.entities.Entity.EntityType;
import com.vjmp.entities.drawable.DrawableEntity;

public class GameDrawableEntityManager extends DrawableEntityManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
public GameDrawableEntityManager(DrawableEntityManager entityManager) {
		super(entityManager);
	}

public GameDrawableEntityManager() {
	super();
}

@Override
	public synchronized void DrawSprites(Graphics g) {

		for(DrawableEntity drawable : list) {
			if(drawable.getType() != EntityType.TRIGGER) {
				drawable.draw(g);
			} 
		}
		
	}
	
}
