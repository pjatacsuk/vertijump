package com.vjmp.managers;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vjmp.InputHandler;
import com.vjmp.chapter.AfterChapterScreen;
import com.vjmp.chapter.Chapter;
import com.vjmp.chapter.Chapter.ChapterState;

public class ChapterManager implements Iterable<Chapter>{
	private List<Chapter> chapter_list = null;
	private InputHandler inputHandler = null;
	private AfterChapterScreen afterChapterScreen = null;
	private int WIDTH = 0;
	private int HEIGHT = 0;
	private int current_chapter = 0;
	public ChapterManager(String path,InputHandler inputHandler,int WIDTH,int HEIGHT) {
		this.chapter_list = new ArrayList<Chapter>();
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.inputHandler = inputHandler;
	try {
		BufferedReader br = new BufferedReader(new FileReader(path));
		
		String line = br.readLine();
		while(line != null) {
			String[] map_properties = line.split("\"");
			String map_path = "./res/" + map_properties[1];
		
			chapter_list.add(new Chapter(map_properties[0],inputHandler,WIDTH,HEIGHT,map_path));
			
			line = br.readLine();
		}
		current_chapter = 0;
		
		chapter_list.get(current_chapter).start();
	
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	}
	
	public void update() {
		ChapterState chapter_state = chapter_list.get(current_chapter).getChapterState();
		if(chapter_state == ChapterState.RUNNING) {
			chapter_list.get(current_chapter).update();
		} else if(chapter_state == ChapterState.FINISHED) {
			//Ugrás a következõ chapterre
			afterChapterScreen = null;
			current_chapter = (current_chapter + 1)%chapter_list.size();
			chapter_list.get(current_chapter).resetMap();
			chapter_list.get(current_chapter).start();
		} else if(chapter_state == ChapterState.DIED) {
			System.out.println("Hello");
			chapter_list.get(current_chapter).resetMap();
			
		} else if(chapter_state == ChapterState.FINISH_SCREEN) {
			if(afterChapterScreen == null) {
				chapter_list.get(current_chapter).stop();
				afterChapterScreen = new AfterChapterScreen(chapter_list.get(current_chapter));
			}
			afterChapterScreen.update();
		}
	}
	public void draw(Graphics g) {
		chapter_list.get(current_chapter).draw(g);
		ChapterState current_chapter_state = chapter_list.get(current_chapter).getChapterState();
		if(current_chapter_state == ChapterState.FINISH_SCREEN) {
			if(afterChapterScreen != null) {
				afterChapterScreen.draw(g);
			}
		}
		
	}

	public void resetChapter() {
		chapter_list.get(current_chapter).resetMap();
		
	}
	public void resetChapterManager() {
		for(Chapter chapter : chapter_list) {
			chapter.resetMap();
		}
		current_chapter = 0;
	}

	@Override
	public Iterator<Chapter> iterator() {
		return chapter_list.iterator();
	}
	
	
	
}
