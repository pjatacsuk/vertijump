package com.vjmp.chapter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.vjmp.InputHandler;
import com.vjmp.chapter.Chapter.ChapterState;
import com.vjmp.gfx.Camera;
import com.vjmp.gfx.Sprite;

public class AfterChapterScreen {
	private InputHandler inputHandler = null;
	private String		 score = null;
	private Chapter chapter = null;
	private Camera camera = null;
	private int box_height = 350;
	private int box_width = box_height * 4/3;
	int pos_x = 0;
	int  pos_y = 0;
	private Sprite background = null; 
	
	

	
	public AfterChapterScreen(Chapter chapter) {
		this.chapter = chapter;
		camera = chapter.getCamera();
		inputHandler = chapter.getInputHandler();
		score = new String(chapter.getScore());
		//pozicionalas
		pos_x = -camera.pos_x + camera.width/2 - box_width / 2;
		pos_y = -camera.pos_y + camera.height/2 - box_height / 2;
		background = new Sprite("./res/score_background.png", pos_x, pos_y,true);
		
	}
	
	
	public void update() {
		handleInput();
	}
	
	public void handleInput() {
		if(inputHandler.ENTER.isPressedOnce()){
			chapter.setChapterState(ChapterState.FINISHED);
			
		}
		
	}


	public void draw(Graphics g) {
		// nincs g.translate(), mivel a Chapter-nek draw-ja utan vagyunk, ahol ez mar megtortent
		drawGrayTransparentBackGround(g);
		
		drawScreenBox(g);
		drawInfo(g);
	
	}


	private void drawGrayTransparentBackGround(Graphics g) {
		g.setColor(new Color(24,24,24,200));
		g.fillRect(-camera.pos_x,-camera.pos_y,camera.width + 20,camera.height+20);
		
	}


	private void drawInfo(Graphics g) {
		g.setFont(new Font("Helvetica",0,32));
		g.setColor(new Color(255,173,0));
		
		FontMetrics metrics = g.getFontMetrics();
		String current_info = "Your score: " + score;
		int your_highscore_pos_x = pos_x + box_width/2 - metrics.stringWidth(current_info)/2 ;
		
		g.drawString(current_info,your_highscore_pos_x, pos_y + 32);
		
		drawTopScoresInfo(g,metrics,pos_y+32+metrics.getHeight() + 10);
		
		g.setFont(new Font("Helvetica",Font.ITALIC,12));
		metrics =  g.getFontMetrics();
		g.drawString("Press Enter to continue", 
					 pos_x + box_width - metrics.stringWidth("Press Enter to continue") - 5,
					 pos_y + box_height - metrics.getHeight() - 5);
	}


	private void drawTopScoresInfo(Graphics g, FontMetrics metrics,int next_pos_y) {
		int n = 5;
		String[] scores = chapter.getChapterHighScoreManager().getHighScores(n);
		int score_pos_x = pos_x + camera.width/2 - metrics.stringWidth("1. "+scores[0]);
		for(int i=0;i<n;i++) {
			if(i==0) {
				g.setColor(new Color(255,228,64));
			}else {
				g.setColor(new Color(255,173,0));
			}
			String score_string = (i+1) + ". " + scores[i];
			
			int score_pos_y = next_pos_y + i * metrics.getHeight() + 10;
			g.drawString(score_string,score_pos_x,score_pos_y);
		}
		
	}


	private void drawScreenBox(Graphics g) {
		//TODO PLACEHOLDER GRAFIKA
		//g.setColor(new Color(66,100,192));
		//g.fillRect(pos_x,pos_y, box_width, box_height);
		background.draw(g);
	}
}
