package com.vjmp.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.vjmp.Game;
import com.vjmp.Game.GameState;
import com.vjmp.gfx.Camera;

/**
 * F�menu, extendeli a Menu oszt�lyt.

 *
 */
public class MainMenu extends Menu {
	
	
	
	private static final int MenuItemPadding = 50;
	
	
	/**
	 * Konstruktor
	 * @param game : {@link Game} - f� Game oszt�ly
	 * @param prevMenu : {@link Menu} - az el�z� men�re mutat
	 * @param camera : {@link Camera} - a camera
	 */
	public MainMenu(Game game,Menu prevMenu,Camera camera) {
		super(game,null,null,camera);
		
		menuItems.add(new GameMenuItem("VertiJumper - The Game",false));
		menuItems.get(0).setHighLightText(true);
		
		menuItems.add(new GameMenuItem("Start New Game",true));
		
		menuItems.add(new GameMenuItem("Resume",false));
		menuItems.add(new HighScoreMenu(game,this,camera));
		
		current_menu = 1;
	
		isMenuActive = true;
	
		
	}
	
	

	/**
	 * Az akci�kat kezeli 
	 * @param gameMenuItem : {@link GameMenuItem} - az aktiv�lt menu
	 */
	@Override
	protected void handleAction(GameMenuItem gameMenuItem) {
		if(gameMenuItem.getName().equals("Start New Game")){
			game.setState(GameState.NEWGAME);
			setGameMenuItemActive("Resume");
			
		} else if(gameMenuItem.getName().equals("Resume")){
			game.setState(GameState.RESUMEGAME);
		} 
	}
	

	/**
	 * Kirajzol�s.
	 */
	@Override
	public void draw(Graphics g) {
		//mainmenu -> � v�gzi
		g.translate(camera.pos_x,camera.pos_y);
		if(isMenuActive) {
			if(switchScreenState == switchScreenState.NONE) {
				if(prevMenu != null) {
					sortGameMenuItemsToScreenOffSet(prevMenu.distanceFromRoot + 1);
				} else {
						sortGameMenuItemsToScreen();
				}
			}
			g.setColor(color);
			g.fillRect(game.getWidth()*distanceFromRoot,0, 
					  game.getWidth(),game.getHeight());
			Font font = new Font("Jokerman", Font.PLAIN, 36);
			g.setFont(font);
			
			
			
			
		
			
			mainMenuDraw(g);
			
		} 
		if(nextMenu != null){
			nextMenu.draw(g);
		}
	}

	/**
	 * A name �ltal kiv�lasztott menu-itemet aktiv�lja
	 * @param name : {@link String} - a kiv�lasztand� n�v
	 */
	private void setGameMenuItemActive(String name) {
		int i = 0;
		while(menuItems.get(i).getName().equals(name) != true){
			i++;
		}
		menuItems.get(i).setActivity(true);
		
	}

	
	
}
