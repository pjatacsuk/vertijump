package com.vjmp.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A "./res/png" mappában lévõ .png kiterjesztésû file-ok útvonalát tölti be egy listába.
 * @author User
 *
 */
public class TextureList implements Iterable<String> {
	private List<String> list = null;
	
	/**
	 * Betölti a mappában lévõ .png kiterjesztésû fileok útvonalát
	 */
	TextureList() {
		list = new ArrayList<String>();
		
			File file = new File("./res/png");
			if(file.isDirectory()) {
				File[] pngs = file.listFiles();
				for(File png : pngs) {
					if(png.isFile() && png.getName().endsWith(".png")) {
						String path = png.getName();
						path = "./res/png/" + path;
						list.add(path);
					}
				}
				
			}
			
	
	}
	
	/**
	 * Visszatér a megadott indexû útvonallal
	 * @param i : int - index
	 * @return path : {@link String} - a megadott indexû útvonal
	 */
	public String GetPath(int i) {
		return list.get(i);	
	}
	
	/**
	 * Visszatér a lista méretével
	 * @return size : int - a lista mérete
	 */
	public int size() {
		return list.size();
	}

	/**
	 * @return iterator : {@link Iterator}
	 */
	@Override
	public Iterator<String> iterator() {
		return list.iterator();
	}

}
