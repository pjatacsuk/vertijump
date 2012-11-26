package com.vjmp.menu;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vjmp.Game;
import com.vjmp.gfx.Camera;

/**
 * A {@link HighScoreListMenu} l�trehoz maximum 10 db menu itemet, amelyek az adott
 * chapter-hez tartoz� legjobb 10 score.
 * @author User
 *
 */
public class HighScoreListMenu extends Menu{
	private String chapterPath;
	
	/**
	 * Konstruktor
	 * @param game : {@link Game} - a j�t�k f� v�ltoz�
	 * @param chapterName : {@link String} - a chapter neve
	 * @param prevMenu : {@link Menu} - az el�z� menu-re mutat� referencia
	 * @param camera : {@link Camera} - a j�t�k Camerea v�ltoz�ja
	 */
	public HighScoreListMenu(Game game,String chapterName,Menu prevMenu,Camera camera) {
		super(game,chapterName,prevMenu,camera);
		color = new Color(35,50,33);
		isActive = true;
		menuItems.add(new GameMenuItem(chapterName,false));
		menuItems.get(0).setHighLightText(true);
		current_menu = 1;
		
		chapterPath = chapterName.replace(" ","");
		try {
			//bet�ltj�k a highscore-okat
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("./res/hs/" + chapterPath + ".txt")));
			int count = 0;
			String line = br.readLine();
			while(line != null && count <= 10) {
				count++;
				menuItems.add(new GameMenuItem(line,true));	
				line = br.readLine();
				
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * Frissiti a highScore-ok list�j�t
	 */
	public void refreshList() {
		menuItems.clear();
		menuItems.add(new GameMenuItem(name,false));
		menuItems.get(0).setHighLightText(true);
		current_menu = 1;
		try {
			//bet�ltj�k a highscore-okat
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("./res/hs/" + chapterPath + ".txt")));
			int count = 0;
			String line = br.readLine();
			while(line != null && count <= 10) {
				count++;
				menuItems.add(new GameMenuItem(line,true));	
				line = br.readLine();
				
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
}
