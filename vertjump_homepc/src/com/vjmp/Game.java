package com.vjmp;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.vjmp.chapter.Chapter;
import com.vjmp.editor.Editor;
import com.vjmp.gfx.Camera;
import com.vjmp.managers.EntityManager;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 180;
	public static final int HEIGHT = (WIDTH)*4/3;
	public static final int SCALE = 3;
	public static final String NAME = "VertiJump - The Game";
	public static 		boolean running = true;
	
    private static Chapter chapter = null;
	private static InputHandler inputHandler = null;
	private static EntityManager spriteManager;
	
	
	
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
		chapter = new Chapter(inputHandler,WIDTH * SCALE,HEIGHT * SCALE,"./res/map.txt");
	}
	
	public void tick() {
	//	placeHolderLogick();
		chapter.update();
		placeHolderLogic();
		
	}
	
	
	
	private void placeHolderLogic() {
		if(inputHandler.W.isPressed()) {
			//player.move(0, -10);
		
		}
		if(inputHandler.A.isPressed()) {
			//player.move(-10,0);
	
		}
		if(inputHandler.S.isPressed()) {
			//player.move(0, 10);
			
		}
		if(inputHandler.D.isPressed()) {
			//player.move(10, 0);
			
		}
		if(inputHandler.ESC.isPressed()) {
			running = false;
		}
		if(inputHandler.P.isPressed()) {
			chapter = new Chapter(inputHandler,WIDTH*SCALE,HEIGHT*SCALE,"./res/map.txt");
		}
		
	}
	
	public void render() {
		BufferStrategy strat = this.getBufferStrategy();
		if(strat == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = strat.getDrawGraphics();
		chapter.draw(g);
		//spriteManager.DrawSprites(g);
		
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
			/*
			if(System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(frames + " " + ticks);
				frames = 0;
				ticks = 0;
			}
			*/
		}
		
	}
	
	
}
