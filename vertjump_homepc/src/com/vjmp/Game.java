package com.vjmp;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.vjmp.gfx.Camera;
import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.SpriteManager;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 180;
	public static final int HEIGHT = (WIDTH)*4/3;
	public static final int SCALE = 3;
	public static final String NAME = "VertiJump";
	public static 		boolean running = true;
	
	private static BufferedImage bg;
	
	private static InputHandler	inputHandler;
	private static Player		player;		
	private static Camera		camera;
	private static Map			map;
	
	private static SpriteManager spriteManager;
	
	
	
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
	}
	
	public synchronized void start() {
		running = true;
		init();
		new Thread(this).start();
	}
	
	public void init() {
		
		spriteManager = new SpriteManager();
		inputHandler  = new InputHandler(this);
		player		  = new Player(inputHandler);
		camera		  = new Camera(WIDTH * SCALE,HEIGHT * SCALE);
		map			  = new Map(WIDTH * SCALE,HEIGHT * SCALE);
		load_resource();
	}
	public void load_resource() {
		bg = null;
		try {
			bg = ImageIO.read(new File("./res/background.png"));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
	
	}
	
	public void tick() {
		placeHolderLogick();
		player.update(map);
		camera.update(player.GetPosX(), player.GetPosY());
		map.update(camera.pos_y);
	}
	
	
	
	private void placeHolderLogick() {
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
		
	}

	public void render() {
		BufferStrategy strat = this.getBufferStrategy();
		if(strat == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = strat.getDrawGraphics();
		
		g.fillRect(0, 0, getWidth(), getHeight());
		DrawBackGround(g);
		g.translate(camera.pos_x,camera.pos_y);
		//g.translate(0,0);

		spriteManager.DrawSprites(g);
		map.draw(g);
		player.draw(g);
		g.dispose();
		
		strat.show();
	}
	
	private void DrawBackGround(Graphics g) {
		double width_draw_count = (double)getWidth() / (double)bg.getWidth();
		double height_draw_count = (double)getHeight() / (double)bg.getHeight();
		for(int i=0;i<width_draw_count+1;i++) 
			for(int j=0;j<height_draw_count+1;j++) {
				g.drawImage(bg, i*bg.getWidth(), j*bg.getHeight(),null);
			}
		
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
			}*/
			
		}
		
	}
	
	public static void main(String[] args) {
		new Game().start();
	}
}
