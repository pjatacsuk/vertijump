package com.vjmp;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.vjmp.gfx.Camera;
import com.vjmp.managers.ChapterManager;
import com.vjmp.managers.SampleManager;
import com.vjmp.menu.MainMenu;
import com.vjmp.sound.Sample;
import com.vjmp.sound.Sample.PlayMode;

/**
 * A j�t�k f� class-a, felel�s a futtat�s�rt, renderel�s�rt,
 * frissit�s��rt. Az ablak megnyit�s�t �s kezel�s�t is ez a class v�gzi.
 * A {@link Canvas} lesz�rmazottja, implement�lja a {@link Runnable} interface-t. 
 *
 */
public class Game extends Canvas implements Runnable {
	public enum GameState{MENU,NEWGAME,RESUMEGAME,RUNGAME};
	
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 180;
	public static final int HEIGHT = (WIDTH)*4/3;
	public static final int SCALE = 3;
	public static final String NAME = "VertiJump - The Game";
	public static 		boolean running = true;
	
    private static boolean gameStarted = false;
	private static  GameState gameState = GameState.MENU;
	
	private MainMenu menu = null;
	private ChapterManager chapterManager = null;
	private InputHandler inputHandler = null;
	private SampleManager sampleManager = null;
	
	
	private JFrame frame;
	
	/**Game konstruktor
	 * 
	
	 */
	public Game() {
		
		setMinimumSize(new Dimension(WIDTH * SCALE,HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE,HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE,HEIGHT*SCALE));
	
		frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this,BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
	
		int x = (dim.width - WIDTH*2*SCALE)/2 - 50;
		int y = (dim.height - HEIGHT*SCALE)/2;
		frame.setLocation(x, y);
		
		

	}
	/** A j�t�k thread-et indit�s�t v�gzi
	 *  
	 */
	public synchronized void start() {
		running = true;
		init();
		
		new Thread(this).start();
	}
	/**
	 * A Game er�forr�sait inicializ�lja
	 * 
	 */
	public void init() {
		
		inputHandler = new InputHandler(this);
		chapterManager = new ChapterManager("./res/map_list.txt",inputHandler,WIDTH * SCALE,HEIGHT * SCALE);
		menu = new MainMenu(this, null, new Camera(getWidth(),getHeight()));
		sampleManager = new SampleManager();
		sampleManager.add(new Sample("bg_music","./res/sound/bgmusic.wav",PlayMode.LOOP));
		sampleManager.playSample("bg_music");
	}
	
	/**
	 * A j�t�kot updateli
	 * 
	 * 
	 */
	public void tick() {
		
		
		switch(gameState) {
		case MENU:
			menu.update();
			break;
		case RESUMEGAME:
		case NEWGAME:			
			break;
		case RUNGAME:
			chapterManager.update();
			break;
			
		}
		updateGameState();
		placeHolderLogic();
		
	}
	
	/**
	 * A GameState-ek k�z�tt val� v�lt�s kezel�se
	 * 
	 */
	private void updateGameState() {
		switch(gameState){
		case NEWGAME:
			chapterManager.resetChapterManager();
			gameState = GameState.RUNGAME;
			gameStarted = true;
			break;
		case RESUMEGAME:
			chapterManager.resetChapter();
			gameState = GameState.RUNGAME;
			break;
		case RUNGAME:
			//running
			break;
		case MENU:
			break;
		
		}
		
	}

	/**

	 * @return inputHandler : {@link InputHandler} - input kezel� 
	 *
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}
	
	/**
	 * Apr�bb inputokat kezel (men�-be l�p�s, p�lya reset)
	 */
	private void placeHolderLogic() {
	
		if(inputHandler.ESC.isPressed()) {
			gameState = GameState.MENU;
			
		}
		if(inputHandler.P.isPressed()) {
			chapterManager.resetChapter();
		}
		
	}
	
	/**
	 * A j�t�kot renderel�s�t v�gzi
	 * 

	 * 
	 */
	public void render() {
		BufferStrategy strat = this.getBufferStrategy();
		if(strat == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = strat.getDrawGraphics();
	
		switch(gameState) {
		case MENU:
			menu.draw(g);
		break;
		case NEWGAME:	
		case RESUMEGAME:
			break;
		case RUNGAME:
			
			chapterManager.draw(g);
			break;
		}
		
		
		g.dispose();
		
		strat.show();
	}
	
	
	
	
	
	/**
	 * Override-olt run f�ggv�ny,
	 * a j�t�k futatt�sa
	 * fix�lja 60fps-re a frameratet
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		
		//h�ny ns jut egy tick-re, c�l a 60 fps
		double nsPerTick = 1000000000.0 / 60.0;
		
		int ticks = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		while(running) {
			long now = System.nanoTime();
			
			//kisz�moljuk h�nyszor kell tickelni
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender  = false;
			while(delta > 0) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			if(shouldRender) {
				frames++;
				render();
			}
			try {
				//sleepelj�k kicsit a thread-et, hogy 
				//drasztikusan cs�kkents�k a cpu haszn�latot
				Thread.sleep(1);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			if(System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(frames + " " + ticks);
				frames = 0;
				ticks = 0;
			}
			
		}
		
	}

	/**
	 * 
	 * @param gameState : GameState - a j�t�k state-j�t t�rolj�a
	 */
	public void setState(GameState gameState) {
		Game.gameState = gameState;
	}

	/**
	 * 
	 * @return gameStarted : boolean - true, ha elindult m�r a j�t�k, false ha nem
	 */
	public static boolean isGameStarted() {
		return gameStarted;
	}

	/**
	 * Be�llitja a gameStarted valtoz�t
	 * @param gameStarted : GameStarted 
	 */
	public static void setGameStarted(boolean gameStarted) {
		Game.gameStarted = gameStarted;
	}

	/**
	 *  
	 * @return ChapterManager : chapterManager - a j�t�k p�ly�it kezel� class
	 */
	public ChapterManager getChapterManager() {
		return chapterManager;
	}
	
}
