package com.vjmp.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextureList implements Iterable<String> {
	private List<String> list = null;
	
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
			
		/*	BufferedReader br = new BufferedReader(new FileReader("./res/png"));
			
			String line = br.readLine();
			while(line != null) {
				line = "./res/" +line;
				list.add(line);
				line = br.readLine();
			}
			br.close(); 
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
	}
	
	public String GetPath(int i) {
		return list.get(i);	
	}
	public int size() {
		return list.size();
	}

	@Override
	public Iterator<String> iterator() {
		return list.iterator();
	}

}
