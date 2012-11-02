package com.vjmp.menu;

import java.awt.Color;

import com.vjmp.Game;
import com.vjmp.chapter.Chapter;
import com.vjmp.gfx.Camera;

public class HighScoreMenu extends Menu {

	
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
	@Override
	protected void handleMenuAction(GameMenuItem gameMenuItem) {
		HighScoreListMenu highScoreListMenu = (HighScoreListMenu)gameMenuItem;
		highScoreListMenu.refreshList();
	}

}
