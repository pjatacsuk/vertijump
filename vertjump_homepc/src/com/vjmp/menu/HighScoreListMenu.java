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
 * A {@link HighScoreListMenu} létrehoz maximum 10 db menu itemet, amelyek az adott
 * chapter-hez tartozó legjobb 10 score.
 * @author User
 *
 */
public class HighScoreListMenu extends Menu{
	private String chapterPath;
	
	/**
	 * Konstruktor
	 * @param game : {@link Game} - a játék fõ változó
	 * @param chapterName : {@link String} - a chapter neve
	 * @param prevMenu : {@link Menu} - az elõzõ menu-re mutató referencia
	 * @param camera : {@link Camera} - a játék Camerea változója
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
			//betöltjük a highscore-okat
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
	 * Frissiti a highScore-ok listáját
	 */
	public void refreshList() {
		menuItems.clear();
		menuItems.add(new GameMenuItem(name,false));
		menuItems.get(0).setHighLightText(true);
		current_menu = 1;
		try {
			//betöltjük a highscore-okat
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
