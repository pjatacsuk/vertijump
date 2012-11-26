package com.vjmp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vjmp.editor.MapEditor;

/**
 * A program fõ classja. A játék illetve editor szálak elinditását végzi.
 * 
 *
 */
public class Program {
	public static Game game;
	public static MapEditor editor;
	public static void main(String[] args)  {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			try {
				String line = br.readLine();
				while(line != null) {
					String[]  words = line.split(" ");
					command(words);	
					line = br.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
	}

	/**
	 * A parancsok feldolgozását végzi
	 * @param words : {@link String} tömb.
	 */
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
