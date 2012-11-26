package com.vjmp.chapter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.vjmp.InputHandler;
import com.vjmp.chapter.Chapter.ChapterState;
import com.vjmp.gfx.Camera;
import com.vjmp.gfx.Sprite;


/**
 * A pálya teljesitése után megjelenõ összesités osztálya.<br>
 * Megjeleniti az elért pontot, illetve a top 5 elért eredményt.
 * 
 *
 */
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
	
	

	/**
	 * Konstruktor<br>
	 * 
	 * @param chapter : {@link Chapter} - Az {@link AfterChapterScreen}-hez tartozó {@link Chapter}.
	 */
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
	
	
	/**
	 * Az update-elést végzi
	 */
	public void update() {
		handleInput();
	}
	
	/**
	 * Az inputokat kezeli.
	 */
	public void handleInput() {
		if(inputHandler.ENTER.isPressedOnce()){
			//mehetünk a következõ Chapter-re
			chapter.setChapterState(ChapterState.FINISHED);
			
		}
		
	}

	/**
	 * A kirajzolást végzõ függvény
	 * @param g : {@link Graphics}
	 */
	public void draw(Graphics g) {
		// nincs g.translate(), mivel a Chapter-nek draw-ja utan vagyunk, ahol ez mar megtortent
		drawGrayTransparentBackGround(g);
		
		drawScreenBox(g);
		drawInfo(g);
	
	}


	/**
	 * Kirajzol egy szürke kissé átlátszó hátteret
	 * @param g : {@link Graphics}
	 */
	private void drawGrayTransparentBackGround(Graphics g) {
		g.setColor(new Color(24,24,24,200));
		g.fillRect(-camera.pos_x,-camera.pos_y,camera.width + 20,camera.height+20);
		
	}


	/**
	 * Kirajzolja a megjelenitendõ információkat:
	 * A jelenleg mért pontot, illetve a legjobb pontokat.
	 * @param g : {@link Graphics}
	 */
	private void drawInfo(Graphics g) {
		g.setFont(new Font("Helvetica",0,32));
		g.setColor(new Color(255,173,0));
		
		//A szövegeket középre állitjuk és kirajzoljuk
		
		FontMetrics metrics = g.getFontMetrics();
		String current_info = "Your score: " + score;
		int your_highscore_pos_x = pos_x + box_width/2 - metrics.stringWidth(current_info)/2 ;
		
		g.drawString(current_info,your_highscore_pos_x, pos_y + 32);
		
		drawTopScoresInfo(g,metrics,pos_y+32+metrics.getHeight() + 10);
		
		g.setFont(new Font("Helvetica",Font.ITALIC,12));
		metrics =  g.getFontMetrics();
		
		//Ezt a szöveget pedig a jobb alsó sarokba helyezzük
		g.drawString("Press Enter to continue", 
					 pos_x + box_width - metrics.stringWidth("Press Enter to continue") - 5,
					 pos_y + box_height - metrics.getHeight() - 5);
	}


	/** 
	 * 
	 * @param g : {@link Graphics}
	 * @param metrics : {@link FontMetrics} - a jelenlegi font tulajdonságait tárolja
	 * @param next_pos_y : int - a következõ score y poziciója
	 */
	private void drawTopScoresInfo(Graphics g, FontMetrics metrics,int next_pos_y) {
		int n = 5;
		//A legjobb 5 score-t kirajzoljuk, esetleges szinben
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


	/**
	 * Kirajzolja az {@link AfterChapterScreen} hátterét.
	 * @param g
	 */
	private void drawScreenBox(Graphics g) {
		background.draw(g);
	}
}
