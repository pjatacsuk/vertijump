package com.vjmp.menu;

import java.awt.Color;

import com.vjmp.Game;
import com.vjmp.chapter.Chapter;
import com.vjmp.gfx.Camera;
import com.vjmp.managers.ChapterManager;

/**
 * L�trehozza a {@link ChapterManager}-b�l kinyert {@link Chapter}-ek �ltal egy olyan menu list�t,
 * amelyekre r�menve, az adott Chapter legjobb 10 score-j�t l�thatjuk
 *
 */
public class HighScoreMenu extends Menu {

	
	/**
	 * Konstruktor
	 * @param game : {@link Game} - f� j�t�kv�ltoz�
	 * @param prevMenu : {@link Menu} - az el�z� menu
	 * @param camera : {@link Camera} - a camera 
	 */
	public HighScoreMenu(Game game,Menu prevMenu,Camera camera) {
		super(game,"HighScores",prevMenu,camera);
		isActive = true;
		color = new Color(37,34,49);
	
		menuItems.add(new GameMenuItem("HighScores",false));
		menuItems.get(0).setHighLightText(true);
		current_menu = 1;
		
	
		for(Chapter chapter : game.getChapterManager()) {
			menuItems.add(new HighScoreListMenu(game,chapter.getName(),this,camera));
		}
	}
	
	/**
	 * Kiv�laszt�s sor�n frissitj�k a kiv�lasztott list�t, a biztosan j� eredm�nyek
	 * rem�n�yben
	 * @param gameMenuItem : {@link GameMenuItem} 
	 */
	@Override
	protected void handleMenuAction(GameMenuItem gameMenuItem) {
		HighScoreListMenu highScoreListMenu = (HighScoreListMenu)gameMenuItem;
		highScoreListMenu.refreshList();
	}

}
