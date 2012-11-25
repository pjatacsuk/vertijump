package com.vjmp.menu;

import java.awt.Color;

import com.vjmp.Game;
import com.vjmp.chapter.Chapter;
import com.vjmp.gfx.Camera;
import com.vjmp.managers.ChapterManager;

/**
 * Létrehozza a {@link ChapterManager}-bõl kinyert {@link Chapter}-ek által egy olyan menu listát,
 * amelyekre rámenve, az adott Chapter legjobb 10 score-ját láthatjuk
 *
 */
public class HighScoreMenu extends Menu {

	
	/**
	 * Konstruktor
	 * @param game : {@link Game} - fõ játékváltozó
	 * @param prevMenu : {@link Menu} - az elõzõ menu
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
	 * Kiválasztás során frissitjük a kiválasztott listát, a biztosan jó eredmények
	 * reménéyben
	 * @param gameMenuItem : {@link GameMenuItem} 
	 */
	@Override
	protected void handleMenuAction(GameMenuItem gameMenuItem) {
		HighScoreListMenu highScoreListMenu = (HighScoreListMenu)gameMenuItem;
		highScoreListMenu.refreshList();
	}

}
