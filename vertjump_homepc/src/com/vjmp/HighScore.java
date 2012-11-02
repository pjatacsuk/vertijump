package com.vjmp;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import com.vjmp.gfx.Camera;

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
	
	public HighScore(String chapterName) {
		stopWatch = new StopWatch();
		offSet = new Point();
		this.chapterName = chapterName;
		score = new String();
	}
	public HighScore(String chapterName, String line) {
		stopWatch = new StopWatch();
		offSet = new Point();
		this.chapterName = chapterName;
		score =  line;
		isFinal = true;
	}
	public void start() {
		stopWatch.reset();
		stopWatch.start();
	}
	public void update(Camera camera) {
		offSet.setLocation(camera.pos_x,camera.pos_y);
		score = formatScore();
	}
	public String formatScore() {
		
		long sec = ((stopWatch.getTime() / 1000)  +  1);
		long msec = stopWatch.getTime() % 100;
		return new String(sec + "." + msec);
	}
	public void draw(Graphics g) {
		Font font = new Font("Jokerman", Font.PLAIN, 24);
		g.setFont(font);
		g.drawString(formatScore(), 24 - offSet.x, 24- offSet.y);
	}
	public void stop() {
		stopWatch.stop();
		
	}
	@Override
	public String toString() {
		if(isFinal == false) {
		return String.valueOf(formatScore()); 
		} else {
			return score;
		}
		
	}
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
