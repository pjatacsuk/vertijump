package com.vjmp.chapter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;

import com.vjmp.InputHandler;
import com.vjmp.Map;
import com.vjmp.Player;
import com.vjmp.editor.Editor;
import com.vjmp.gfx.Camera;
import com.vjmp.managers.ChapterHighScoreManager;
import com.vjmp.utilities.HighScore;

public class Chapter {
	public enum ChapterState{FINISHED,DIED,RUNNING,STALLING, FINISH_SCREEN};
	
	
	private Map map = null;
	private Camera camera = null;
	private Player player = null;
	private InputHandler inputHandler = null;
	private static BufferedImage bg;
	private int WIDTH = 0;
	private int HEIGHT = 0;
	private String path = null;
	private HighScore highScore = null;
	private ChapterHighScoreManager chapterHighScoreManager = null;
	private String chapterName;
	private Thread mapUpdateThread = null;
	
	private ChapterState chapterState = ChapterState.STALLING;
	
	private TriggerHandler triggerHandler = null;
	public boolean isReadyToUpdate = true;
	
	public Chapter(String name,InputHandler inputHandler,int WIDTH,int HEIGHT,String map_path) {
			player = new Player(inputHandler);
			camera = new Camera(WIDTH,HEIGHT);
			map = new Map(WIDTH,HEIGHT,true);
			path = map_path;
			chapterName = name;
			this.WIDTH = WIDTH;
			this.HEIGHT = HEIGHT;
			this.inputHandler = inputHandler;
			load_resource();
			highScore = new HighScore(chapterName);
			chapterHighScoreManager = new ChapterHighScoreManager(chapterName);
			map.setCamera(camera);
			
			try {
				load(map_path);
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
			
	}
	public void load_resource() {
		bg = null;
		try {
			bg = ImageIO.read(new File("./res/background.png"));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
	
	}
	private void load(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
			 this.path = path;
			 ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			 Editor editor = (Editor)ois.readObject();
			 map = new Map(editor.getMap(),false);
			 triggerHandler = new TriggerHandler(this,map.getTriggerEntityManager());
			 player = new Player(inputHandler);
			 player.setLocation(editor.GetPlayerLocation());
			 camera.pos_y =0;
			 ois.close();
			 map.isEditor = false;
			 start();
	 }
	 public void start() {
		 chapterState = ChapterState.RUNNING;
	
		 map.isEditor = false;
		
		 if(highScore == null) {
		 	highScore = new HighScore(chapterName);
		 	
		 }
		 highScore.start();
	 }
	 public void stop() {
		 highScore.stop();
		 chapterHighScoreManager.add(new HighScore(chapterName,highScore.toString()));
		 chapterHighScoreManager.saveToFile();
	 }
	 public void resetMap() {
		  try {
		
			load(path);
			start();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 public void update() {
		 	triggerHandler.update();
		 	player.update(map);
			camera.update(player.GetPosX(), player.GetPosY());
			map.update(camera);
			highScore.update(camera);
			updatePlayerFallDeath(); 
	 }
	 private void updatePlayerFallDeath() {
		if(camera.pos_y + player.GetPosY() > HEIGHT + 10) {
			resetMap();
		}
	}
	public ChapterState getChapterState() { 
		 return chapterState;
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
				
			}
			if(inputHandler.P.isPressed()) {
				try {
					load("./res/map.txt");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

		private void DrawBackGround(Graphics g) {
			double width_draw_count = (double)WIDTH / (double)bg.getWidth();
			double height_draw_count = (double)HEIGHT / (double)bg.getHeight();
			for(int i=0;i<width_draw_count+1;i++) 
				for(int j=0;j<height_draw_count+1;j++) {
					g.drawImage(bg, i*bg.getWidth(), j*bg.getHeight(),null);
				}
			
		}
	 public void draw(Graphics g) {
		    g.fillRect(0, 0, WIDTH, HEIGHT);
			DrawBackGround(g);
			g.translate(camera.pos_x,camera.pos_y);
			//g.translate(0,0);
			map.draw(g);
			player.draw(g);
			if(chapterState == ChapterState.RUNNING) {
				highScore.draw(g);
			}
			
	 }
	public void setChapterState(ChapterState state) {
		chapterState = state;
		
	}
	public String getName() {
		return chapterName;
	}
	public InputHandler getInputHandler() {
		return inputHandler;
	}
	public String getScore() {
		return highScore.formatScore();
	}
	public Camera getCamera() {
		return camera;
	}
	public ChapterHighScoreManager getChapterHighScoreManager() {
		return chapterHighScoreManager;
	}
}
