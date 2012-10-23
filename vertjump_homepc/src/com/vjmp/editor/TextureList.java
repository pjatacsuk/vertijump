package com.vjmp.editor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vjmp.gfx.Sprite;

public class TextureList {
	private List<String> list = null;
	
	TextureList() {
		list = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("./res/textures.txt"));
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
		}
	}
	
	public String GetPath(int i) {
		return list.get(i);	
	}
	public int size() {
		return list.size();
	}

}
