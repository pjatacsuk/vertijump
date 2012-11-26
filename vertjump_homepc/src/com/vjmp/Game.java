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
 * A játék fõ class-a, felelõs a futtatásért, renderelésért,
 * frissitéséért. Az ablak megnyitását és kezelését is ez a class végzi.
 * A {@link Canvas} leszármazottja, implementálja a {@link Runnable} interface-t. 
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
	/** A játék thread-et inditását végzi
	 *  
	 */
	public synchronized void start() {
		running = true;
		init();
		
		new Thread(this).start();
	}
	/**
	 * A Game erõforrásait inicializálja
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
	 * A játékot updateli
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
	 * A GameState-ek között való váltás kezelése
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

	 * @return inputHandler : {@link InputHandler} - input kezelõ 
	 *
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}
	
	/**
	 * Apróbb inputokat kezel (menü-be lépés, pálya reset)
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
	 * A játékot renderelését végzi
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
	 * Override-olt run függvény,
	 * a játék futattása
	 * fixálja 60fps-re a frameratet
	 */
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		
		//hány ns jut egy tick-re, cél a 60 fps
		double nsPerTick = 1000000000.0 / 60.0;
		
		int ticks = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		while(running) {
			long now = System.nanoTime();
			
			//kiszámoljuk hányszor kell tickelni
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
				//sleepeljük kicsit a thread-et, hogy 
				//drasztikusan csökkentsük a cpu használatot
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
	 * @param gameState : GameState - a játék state-jét tároljáa
	 */
	public void setState(GameState gameState) {
		Game.gameState = gameState;
	}

	/**
	 * 
	 * @return gameStarted : boolean - true, ha elindult már a játék, false ha nem
	 */
	public static boolean isGameStarted() {
		return gameStarted;
	}

	/**
	 * Beállitja a gameStarted valtozót
	 * @param gameStarted : GameStarted 
	 */
	public static void setGameStarted(boolean gameStarted) {
		Game.gameStarted = gameStarted;
	}

	/**
	 *  
	 * @return ChapterManager : chapterManager - a játék pályáit kezelõ class
	 */
	public ChapterManager getChapterManager() {
		return chapterManager;
	}
	
}
