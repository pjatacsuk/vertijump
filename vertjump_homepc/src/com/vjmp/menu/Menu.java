package com.vjmp.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.vjmp.Game;
import com.vjmp.gfx.Camera;

public abstract class Menu extends GameMenuItem{
	public enum SwitchScreenState {
		NEXT_MENU,NONE,PREV_MENU
	};

	protected List<GameMenuItem> menuItems = null;
	protected Game game = null;
	protected int current_menu = 0;

	protected static final int MenuItemPadding = 50;

	
	protected boolean isMenuActive = false;
	protected Menu nextMenu = null;
	protected Menu prevMenu = null;
	protected SwitchScreenState switchScreenState = SwitchScreenState.NONE;
	protected int distanceFromRoot = 0;
	protected Camera camera = null;
	protected Color color = null;

	public Menu(Game game) {
		menuItems = new ArrayList<GameMenuItem>();
		current_menu = 0;
		this.game = game;
		menuItemType = MenuItemType.MENU;
	}
	public Menu(Game game,String name,Menu prevMenu,Camera camera) {
		super(name);
		this.game = game;
		this.prevMenu = prevMenu;
		this.camera =  camera;
		if(prevMenu != null) {
			this.distanceFromRoot = prevMenu.distanceFromRoot + 1;
		} else {
			this.distanceFromRoot = 0;
		}
		color = new Color(17,23,26);
		
		this.isMenuActive = true;
		
		menuItems = new ArrayList<GameMenuItem>();
		current_menu = 0;
		
		menuItemType = MenuItemType.MENU;
		
	}
	
	public void addNextMenu(Menu menu) {
		nextMenu = menu;
	}
	
	protected void sortGameMenuItemsToScreen() {
		int pos_x = game.getWidth() / 2;
		int pos_y = game.getHeight() / 2 - (menuItems.size() * MenuItemPadding)
				/ 2;
		for (int i = 0; i < menuItems.size(); i++) {
			if(menuItems.get(i).isHighLightText) {
				menuItems.get(i).setLocation(
						pos_x - menuItems.get(i).getWidth() / 2,
						pos_y - 100);
			} else {
			menuItems.get(i).setLocation(
					pos_x - menuItems.get(i).getWidth() / 2,
					pos_y + i * MenuItemPadding);
			}
		}

	}
	protected void sortGameMenuItemsToScreenOffSet(int distaFromRoot) {
		int pos_x = game.getWidth() / 2;
		int pos_y = game.getHeight() / 2 - (menuItems.size() * MenuItemPadding)
				/ 2;
		for (int i = 0; i < menuItems.size(); i++) {
			if(menuItems.get(i).isHighLightText) {
				int highlight_pos_y = pos_y;
				if(pos_y - 100 < 0) {
					highlight_pos_y = 50;
				} else {
					highlight_pos_y = pos_y - 50;
				}
				menuItems.get(i).setLocation(
						pos_x + (distaFromRoot * game.getWidth()) - menuItems.get(i).getWidth() / 2,
						highlight_pos_y);
			} else {
			menuItems.get(i).setLocation(
					pos_x + (distaFromRoot * game.getWidth()) - menuItems.get(i).getWidth() / 2,
					pos_y + i * MenuItemPadding);
			}
		}

	}

	public void mainMenuDraw(Graphics g) {
		for (GameMenuItem gameMenuItem : menuItems) {
			if(gameMenuItem.menuItemType == MenuItemType.ACTION) {
				if (gameMenuItem.selected() || gameMenuItem.isHighLight()) {
					g.setColor(Color.RED);
				} else if (gameMenuItem.getActivity() == false) {
					g.setColor(Color.darkGray);
				}  else {
					g.setColor(Color.GRAY);
				}
				gameMenuItem.draw(g);
				} else if(gameMenuItem.menuItemType == MenuItemType.MENU) {
							if(gameMenuItem.selected()) {
								g.setColor(Color.RED);
									
							} else {
								g.setColor(Color.GRAY);
							}
						gameMenuItem.drawMenuName(g);
				}
		}
	}

	public void update() {
			if(isMenuActive) {
				switch(switchScreenState){
				case NEXT_MENU:
					switchToNextScreen();
					break;
				case PREV_MENU:
					switchToPrevScreen();
					break;
				case NONE:
					handleInput();
					break;
				
				} 
			} 
			if(nextMenu!=null && switchScreenState == SwitchScreenState.NONE) {
				nextMenu.update();
			}
			
			
			
	}

	
	private void handleInput() {
		if (game.getInputHandler().W.isPressedOnce()) {

			menuItems.get(current_menu).deselect();
			do {
				current_menu = (current_menu - 1) % menuItems.size();
				if (current_menu < 0) {
					current_menu = menuItems.size() - 1;
				}
			} while (menuItems.get(current_menu).getActivity() != true);

		}
		if (game.getInputHandler().S.isPressedOnce()) {
			menuItems.get(current_menu).deselect();
			do {
				current_menu = (current_menu + 1) % menuItems.size();
			} while (menuItems.get(current_menu).getActivity() != true);
		}
		if (game.getInputHandler().ENTER.isPressedOnce()) {
			handleEnter();
			
		}
		if (game.getInputHandler().ESC.isPressedOnce()) {
			handleESC();
			
		}
		
		menuItems.get(current_menu).select();

	}
	private void handleESC() {
		if(prevMenu != null) {
			switchScreenState = SwitchScreenState.PREV_MENU;
			prevMenu.isMenuActive = true;
		
			
		}
		
	}
	private void handleEnter() {
		if(menuItems.get(current_menu).menuItemType == MenuItemType.ACTION) {
			handleAction(menuItems.get(current_menu));
		} else if(menuItems.get(current_menu).menuItemType == MenuItemType.MENU) {
			nextMenu = (Menu) menuItems.get(current_menu);
			switchScreenState = SwitchScreenState.NEXT_MENU;
			nextMenu.isMenuActive = true;
		
			handleMenuAction(menuItems.get(current_menu));
		}
		
	}
	protected void handleMenuAction(GameMenuItem gameMenuItem) {
		// TODO Auto-generated method stub
		
	}
	protected void handleAction(GameMenuItem gameMenuItem) {
		// TODO Auto-generated method stub
		
	}
	private void setGameMenuItemActive(String name) {
		int i = 0;
		while(menuItems.get(i).getName().equals(name) != true){
			i++;
		}
		menuItems.get(i).setActivity(true);
		
	}
	protected void switchToNextScreen() {
		if(camera.pos_x > (-1 * nextMenu.distanceFromRoot*game.getWidth())) {
				camera.pos_x -= 15;
		} else {
			isMenuActive = false;
			switchScreenState = SwitchScreenState.NONE;
			nextMenu.switchScreenState = SwitchScreenState.NONE;
			
		}	
		
	}
	
	protected void switchToPrevScreen() {
		if(camera.pos_x < (-1 * prevMenu.distanceFromRoot*game.getWidth())) {
				camera.pos_x += 15;
		} else {
			isMenuActive = false;
			switchScreenState = SwitchScreenState.NONE;
			prevMenu.switchScreenState = SwitchScreenState.NONE;
		}
		
	}
	@Override
	public void drawMenuName(Graphics g) {
		Font font = new Font("Jokerman", Font.PLAIN, 24);
		g.setFont(font);
		if(width == 0) {
			createWidth(g);
		}
		
		g.drawString(name, position.x,position.y);
		
	}
	public void draw(Graphics g) {
		if(isMenuActive) {
			if(switchScreenState == switchScreenState.NONE) {
				if(prevMenu != null) {
					sortGameMenuItemsToScreenOffSet(distanceFromRoot);
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
}
