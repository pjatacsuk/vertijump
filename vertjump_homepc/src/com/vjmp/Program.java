package com.vjmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vjmp.editor.MapEditor;

public class Program {
	public static Game game;
	public static MapEditor editor;
	public static void main(String[] args)  {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//	String line = br.readLine();
			String line = "mapeditor";
			while(line != null) {
				String[]  words = line.split(" ");
				command(words);
				if(line.equals("mapeditor")) {
					line = "game";
				} else {
					line = null;
				} 
			
			}
	}

	private static void command(String[] words) {
		if(words[0].equals("game")) {
			game = new Game();
			game.start();
			
		}else if(words[0].equals("mapeditor")) {
			editor = new MapEditor();
			editor.start();
		}
		
	}
}
