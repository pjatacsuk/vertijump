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

/**
 * A {@link Chapter}-ek manageles�t v�gz� oszt�ly. Frissiti, kirajzolja, rendezi ezeket.
 * Implement�lja az Iterable interface-t
 *
 */
public class ChapterManager implements Iterable<Chapter>{
	private List<Chapter> chapter_list = null;
	private InputHandler inputHandler = null;
	private AfterChapterScreen afterChapterScreen = null;
	private int WIDTH = 0;
	private int HEIGHT = 0;
	private int current_chapter = 0;
	
	/**
	 * Konstruktor
	 * @param path : {@link String} - a megadott el�r�si �tvonal a chapter list�t tartalmaz� file-hoz
	 * @param inputHandler : {@link InputHandler} - az input kezel�
	 * @param WIDTH : int - az ablak sz�less�ge
	 * @param HEIGHT : int - az ablak magass�ga
	 */
	public ChapterManager(String path,InputHandler inputHandler,int WIDTH,int HEIGHT) {
		this.chapter_list = new ArrayList<Chapter>();
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.inputHandler = inputHandler;
	try {
		//bet�ltj�k a chaptereket, a megadott map_lista alapj�n (path)
		BufferedReader br = new BufferedReader(new FileReader(path));
		
		String line = br.readLine();
		while(line != null) {
			String[] map_properties = line.split("\"");
			String map_path = "./res/" + map_properties[1];
		
			chapter_list.add(new Chapter(map_properties[0],inputHandler,WIDTH,HEIGHT,map_path));
			
			line = br.readLine();
		}
		current_chapter = 0;
		
	
	
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	}
	
	/**
	 * Update-eli a {@link Chapter}-eket a {@link ChapterState} alapj�n.
	 * Amennyiben egyik {@link Chapter} el�ri a FINISH -ed �llapotot, v�runk az
	 * {@link AfterChapterScreen} bez�r�s�ra �s  ugrunk a k�vetkez�re.
	 * Amennyiben el�ri a DIED �llapotot, resetelj�k az adott {@link Chapter}-t.
	 */
	public void update() {
		ChapterState chapter_state = chapter_list.get(current_chapter).getChapterState();
		if(chapter_state == ChapterState.RUNNING) {
			chapter_list.get(current_chapter).update();
		} else if(chapter_state == ChapterState.FINISHED) {
			
			//Ugr�s a k�vetkez� chapterre,afterChapterScreen-t null-ozzuk
			afterChapterScreen = null;
			current_chapter = (current_chapter + 1)%chapter_list.size();
			chapter_list.get(current_chapter).resetMap();
			chapter_list.get(current_chapter).start();
		} else if(chapter_state == ChapterState.DIED) {
			//kezdj�k a p�ly�t el�lr�l
			chapter_list.get(current_chapter).resetMap();
			
		} else if(chapter_state == ChapterState.FINISH_SCREEN) {
			if(afterChapterScreen == null) {
				//ha m�g nincs afterChapterScreen-n�nk akkor csin�lunk
				chapter_list.get(current_chapter).stop();
				afterChapterScreen = new AfterChapterScreen(chapter_list.get(current_chapter));
			}
			afterChapterScreen.update();
		}
	}
	
	/**
	 * Kirajzoljuk az adott {@link Chapter}, ha v�ge van megjelenitj�k az adott {@link AfterChapterScreen}-t.
	 * @param g : {@link Graphics}
	 */
	public void draw(Graphics g) {
		chapter_list.get(current_chapter).draw(g);
		ChapterState current_chapter_state = chapter_list.get(current_chapter).getChapterState();
		if(current_chapter_state == ChapterState.FINISH_SCREEN) {
			if(afterChapterScreen != null) {
				afterChapterScreen.draw(g);
			}
		}
		
	}

	/**
	 * Reseteli az aktu�lis {@link Chapter}-t.
	 */
	public void resetChapter() {
		chapter_list.get(current_chapter).resetMap();
		
	}
	
	/**
	 * Reseteli az �sszes {@link Chapter}-t. Alap�llapotba �llitja a {@link ChapterManager}-t.
	 */
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
