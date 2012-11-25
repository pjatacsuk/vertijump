package com.vjmp.managers;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.vjmp.Game;
import com.vjmp.entities.Entity.EntityType;
import com.vjmp.entities.drawable.DrawableEntity;
/**
 * {@link DrawableEntityManager} a {@link Game} oszt�lyhoz specifikusan igazitva.
 * @author User
 *
 */
public class GameDrawableEntityManager extends DrawableEntityManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * M�sol� konstruktor
	 * @param entityManager : {@link EntityManager} - m�soland� konstruktor
	 */
	public GameDrawableEntityManager(DrawableEntityManager entityManager) {
		super(entityManager);
	}

	/**
	 * Konstruktor
	 */
	public GameDrawableEntityManager() {
		super();
	}

	/**
	 * Kirajzolja a sprite-okat
	 * @param g : {@link Graphics}
	 */
	@Override
	public synchronized void DrawSprites(Graphics g) {

		for(DrawableEntity drawable : list) {
			if(drawable.getType() != EntityType.TRIGGER) {
				drawable.draw(g);
			} 
		}
		
	}
	
}
