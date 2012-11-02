package com.vjmp.managers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vjmp.HighScore;

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
			String line = br.readLine();
			while(line != null) {
				highscore_list.add(new HighScore(chapterName,line));
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
		
		Collections.sort(highscore_list);
		for(HighScore  hs :highscore_list) {
			System.out.println(hs);
		}

	}
	public void add(HighScore hs) {
		highscore_list.add(hs);
		Collections.sort(highscore_list);
	}
	
	public void saveToFile() {
		String chapterPath = chapterName.replace(" ","");
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("./res/hs/"+chapterPath+".txt"));
			for(HighScore hs : highscore_list) {
				pw.println(hs);
			}
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
}
