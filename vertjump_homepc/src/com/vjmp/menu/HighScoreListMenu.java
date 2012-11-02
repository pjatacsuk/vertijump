package com.vjmp.menu;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vjmp.Game;
import com.vjmp.gfx.Camera;

public class HighScoreListMenu extends Menu{
	private String chapterPath;
	
	public HighScoreListMenu(Game game,String chapterName,Menu prevMenu,Camera camera) {
		super(game,chapterName,prevMenu,camera);
		color = new Color(35,50,33);
		isActive = true;
		menuItems.add(new GameMenuItem(chapterName,false));
		menuItems.get(0).setHighLightText(true);
		current_menu = 1;
		
		chapterPath = chapterName.replace(" ","");
		try {
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
	
	public void refreshList() {
		menuItems.clear();
		menuItems.add(new GameMenuItem(name,false));
		menuItems.get(0).setHighLightText(true);
		current_menu = 1;
		try {
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
