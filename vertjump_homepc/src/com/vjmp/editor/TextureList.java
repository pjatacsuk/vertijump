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
 * A "./res/png" mapp�ban l�v� .png kiterjeszt�s� file-ok �tvonal�t t�lti be egy list�ba.
 * @author User
 *
 */
public class TextureList implements Iterable<String> {
	private List<String> list = null;
	
	/**
	 * Bet�lti a mapp�ban l�v� .png kiterjeszt�s� fileok �tvonal�t
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
	 * Visszat�r a megadott index� �tvonallal
	 * @param i : int - index
	 * @return path : {@link String} - a megadott index� �tvonal
	 */
	public String GetPath(int i) {
		return list.get(i);	
	}
	
	/**
	 * Visszat�r a lista m�ret�vel
	 * @return size : int - a lista m�rete
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
