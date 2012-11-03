package com.vjmp.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vjmp.utilities.HighScore;

public class ChapterHighScoreManager {
	List<HighScore> highscore_list = null;
	String chapterName = null;

	
	public ChapterHighScoreManager(String chapterName) {
		highscore_list = new ArrayList<HighScore>();
		this.chapterName = chapterName;
		String chapterPath = chapterName.replace(" ","");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("./res/hs/" + chapterPath + ".txt")));
			int count = 0;
			String line = br.readLine();
			count++;
			while(line != null && count <= 100) {
				highscore_list.add(new HighScore(chapterName,line));
				line = br.readLine();
				count++;
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
	public void add(HighScore hs) {
		highscore_list.add(hs);
		Collections.sort(highscore_list);
	}
	
	public void saveToFile() {
		//mivel mindig csak a legjobb 100-at olvassuk be, ezert max 100 highscore-t mentunk
		String chapterPath = chapterName.replace(" ","");
		try {
			//felulirjuk a file-t
			String file_path = "./res/hs/"+chapterPath+".txt";
			File file = new File(file_path);
			file.delete();
			
			PrintWriter pw = new PrintWriter(new FileWriter(file_path));
			
			for(HighScore hs : highscore_list) {
				pw.println(hs);
			}
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public String[] getHighScores(int n) {
		String[] ret = new String[n];
		for(int i=0;i<n;i++) {
			if(i >= highscore_list.size()){
				ret[i] = new String("-.--");
			} else {
				ret[i] = highscore_list.get(i).toString();
			}
		}
		
		return ret;
	}
	
}
