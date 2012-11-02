package com.vjmp;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.vjmp.gfx.Camera;
import com.vjmp.managers.ChapterManager;
import com.vjmp.managers.EntityManager;
import com.vjmp.menu.MainMenu;

public class Game extends Canvas implements Runnable {
	public enum GameState{MENU,NEWGAME,RESUMEGAME,RUNGAME};
	
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 180;
	public static final int HEIGHT = (WIDTH)*4/3;
	public static final int SCALE = 3;
	public static final String NAME = "VertiJump - The Game";
	public static 		boolean running = true;
	
    private static boolean gameStarted = false;
	private static ChapterManager chapterManager = null;
	private static InputHandler inputHandler = null;
	private static EntityManager spriteManager;
	private static String frame_tick = null;
	private static MainMenu menu = null;
	private static GameState gameState = GameState.MENU;
	
	
	private JFrame frame;
	
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
	
	public synchronized void start() {
		running = true;
		init();
		
		new Thread(this).start();
	}
	
	public void init() {
		
		spriteManager = new EntityManager();
		inputHandler = new InputHandler(this);
		chapterManager = new ChapterManager("./res/map_list.txt",inputHandler,WIDTH * SCALE,HEIGHT * SCALE);
		menu = new MainMenu(this, null, new Camera(getWidth(),getHeight()));
	}
	
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
		}
		
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}
	private void placeHolderLogic() {
	
		if(inputHandler.ESC.isPressed()) {
			gameState = GameState.MENU;
			
		}
		if(inputHandler.P.isPressed()) {
			chapterManager.resetChapter();
		}
		
	}
	
	public void render() {
		BufferStrategy strat = this.getBufferStrategy();
		if(strat == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = strat.getDrawGraphics();
	
		switch(gameState) {
		case MENU:
	//		g.setColor(new Color(17,23,26));
	//		g.fillRect(0, 0, getWidth(),getHeight());
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
	
	
	
	
	
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0 / 60.0;
		
		int ticks = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		while(running) {
			long now = System.nanoTime();
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
			
			if(System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(frames + " " + ticks);
				frames = 0;
				ticks = 0;
			}
			
		}
		
	}

	public void setState(GameState gameState) {
		this.gameState = gameState;
	}

	public static boolean isGameStarted() {
		return gameStarted;
	}

	public static void setGameStarted(boolean gameStarted) {
		Game.gameStarted = gameStarted;
	}

	
	public ChapterManager getChapterManager() {
		return chapterManager;
	}
	
}
