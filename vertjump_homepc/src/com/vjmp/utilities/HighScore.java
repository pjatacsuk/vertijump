package com.vjmp.utilities;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import com.vjmp.chapter.Chapter;
import com.vjmp.gfx.Camera;

/**
 * 
 * A {@link Chapter}-hez tartozó HighScore az adott pálya teljesitése alapján meghatároz egy pontot,
 * amit tárol.
 *
 */
public class HighScore implements Comparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StopWatch stopWatch = null;
	private Point offSet = null;
	private String chapterName = null;
	private String score = null;
	private boolean isFinal = false; 
	
	/**
	 * Konstruktor
	 * @param chapterName : {@link String} - a {@link Chapter} neve amihez tartozik.
	 */
	public HighScore(String chapterName) {
		stopWatch = new StopWatch();
		offSet = new Point();
		this.chapterName = chapterName;
		score = new String();
	}
	
	/**
	 * Konstruktor
	 * @param chapterName : {@link String} - a {@link Chapter} neve amihez tartozik.
	 * @param line : {@link String} - az adott score
	 */
	public HighScore(String chapterName, String line) {
		stopWatch = new StopWatch();
		offSet = new Point();
		this.chapterName = chapterName;
		score =  line;
		isFinal = true;
	}
	
	/**
	 * Elinditja a highScore mérését.
	 */
	public void start() {
		stopWatch.reset();
		stopWatch.start();
	}
	
	/**
	 * Frissiti a highScore-t a camera függvényében
	 * @param camera : {@link Camera} 
	 */
	public void update(Camera camera) {
		offSet.setLocation(camera.pos_x,camera.pos_y);
		score = formatScore();
	}
	
	/**
	 * Formatálja a score-t emberileg elfogadható formátumra, majd visszaadja
	 * @return ret  : {@link String} - a formázott highScore 
	 */
	public String formatScore() {
		
		long sec = ((stopWatch.getTime() / 1000) );
		long msec = stopWatch.getTime() % 1000;
		return new String(sec + "." + msec);
	}
	
	/**
	 * Kirajzolja a highScore-t
	 * @param g
	 */
	public void draw(Graphics g) {
		Font font = new Font("Jokerman", Font.PLAIN, 24);
		g.setFont(font);
		g.drawString(formatScore(), 24 - offSet.x, 24- offSet.y);
	}
	
	/**
	 * Megállitja a highScore mérését.
	 */
	public void stop() {
		stopWatch.stop();
		
	}
	
	/**
	 * String-é alakitja a HighScore-t(formázza elõtte)
	 */
	@Override
	public String toString() {
		if(isFinal == false) {
		return String.valueOf(formatScore()); 
		} else {
			return score;
		}
		
	}
	
	/**
	 * comapreTo összehasonlitás, az összehasonlitás a score alapján történik
	 */
	@Override
	public int compareTo(Object o) {
		HighScore hs = (HighScore)o;
		if(Double.valueOf(score) < Double.valueOf(hs.score)) {
			return -1;
		} else if(Double.valueOf(score) > Double.valueOf(hs.score)) {
			return 1;
		} else {
			return 0;
		}
	}
	
	
}
