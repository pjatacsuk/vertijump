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


/**
 * A tényleges pályát kezeli. Megjeleniti, frissiti,betölti, elinditja, pause-olja, reseteli.
 * 
 *
 */
public class Chapter {
	public enum ChapterState{FINISHED,DIED,RUNNING,STALLING, FINISH_SCREEN};
	
	
	private Map map = null;
	private Camera camera = null;
	private Player player = null;
	private InputHandler inputHandler = null;
	private TriggerHandler triggerHandler = null;
	
	private static BufferedImage bg;
	private int WIDTH = 0;
	private int HEIGHT = 0;
	private String path = null;
	private String chapterName;
	
	private HighScore highScore = null;
	private ChapterHighScoreManager chapterHighScoreManager = null;
	
	private ChapterState chapterState = ChapterState.STALLING;	
	public boolean isReadyToUpdate = true;
	
	/**Konstruktor
	 * 
	 * @param name : {@link String} - a pálya neve
	 * @param inputHandler : {@link InputHandler} - az input kezelése
	 * @param WIDTH : int - a pálya szélessége
	 * @param HEIGHT : int - a pálya magassága
	 * @param map_path : {@link String} - a pályát tartalmazó file útvonala
	 */
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
	
	/**
	 * Betölti a chapter szükséges erõforrásokat
	 */
	public void load_resource() {
		bg = null;
		try {
			bg = ImageIO.read(new File("./res/background.png"));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
	
	}
	
	/**
	 * Betölti a chaptert a megadott elérési út alapján
	 * @param path : {@link String} - a pálya elérési útja
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void load(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
			 this.path = path;
			 ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			 //ronda megoldás, refactorra vár
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
	
	/**
	 * Elinditja a chaptert, a score mérést, beállitja a chapterState-t RUNNING-ra.
	 * 
	 */
	 public void start() {
		 chapterState = ChapterState.RUNNING;
	
		 map.isEditor = false;
		
		 if(highScore == null) {
		 	highScore = new HighScore(chapterName);
		 	
		 }
		 highScore.start();
	 }
	 
	 /**
	  * Megállitja a chaptert. Kiszámitja a score-t, majd hozzáadja a highscore-okhoz és el is menti.
	  */
	 public void stop() {
		 highScore.stop();
		 chapterHighScoreManager.add(new HighScore(chapterName,highScore.toString()));
		 chapterHighScoreManager.saveToFile();
	 }
	 
	 /**
	  * Reseteli a pályát.
	  */
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
	 
	 /**
	  * Elvégzi a chapter frissitését. Frissiti a {@link Player}-t, a {@link Map}-et,a {@link HighScore}-t, a {@link Camera}-t,
	  * a {@link TriggerHandler}-t.
	  *
	  */
	 public void update() {
		 	triggerHandler.update();
		 	player.update(map);
			camera.update(player.GetPosX(), player.GetPosY());
			map.update(camera);
			highScore.update(camera);
			updatePlayerFallDeath(); 
	 }
	 
	 /**
	  * Vizsgálja, le-e esett a player, ha leesett resetel.
	  */
	 private void updatePlayerFallDeath() {
		if(camera.pos_y + player.GetPosY() > HEIGHT + 10) {
			resetMap();
		}
	}
	
	 /**
	  * 
	  * @return chapterState : {@link ChapterState} - a jelenlegi chapter állapot
	  */
	public ChapterState getChapterState() { 
		 return chapterState;
	 }
	
	/**
	 * Kirajzolja a hátteret.
	 * @param g : {@link Graphics}
	 */
	 private void DrawBackGround(Graphics g) {
		 //megnézzük hányszor kell ismételni, hogy elfedje a képernyõt
			double width_draw_count = (double)WIDTH / (double)bg.getWidth();
			double height_draw_count = (double)HEIGHT / (double)bg.getHeight();
			for(int i=0;i<width_draw_count+1;i++) 
				for(int j=0;j<height_draw_count+1;j++) {
					g.drawImage(bg, i*bg.getWidth(), j*bg.getHeight(),null);
				}
			
	 }
	 
	 /**
	  * A kirajzolást végzi.
	  * @param g : {@link Graphics}
	  */
	 public void draw(Graphics g) {
		    g.fillRect(0, 0, WIDTH, HEIGHT);
			DrawBackGround(g);
			g.translate(camera.pos_x,camera.pos_y);
			
			map.draw(g);
			player.draw(g);
			if(chapterState == ChapterState.RUNNING) {
				highScore.draw(g);
			}
			
	 }
	 
	 /**
	  * Beállitja a kivánt {@link ChapterState}-t.
	  * @param state : {@link ChapterState}
	  */
	public void setChapterState(ChapterState state) {
		chapterState = state;
		
	}
	
	/**
	 * 
	 * @return chapterName : {@link String}
	 */
	public String getName() {
		return chapterName;
	}
	
	/**
	 * 
	 * @return inputHandler : {@link InputHandler}
	 */
	public InputHandler getInputHandler() {
		return inputHandler;
	}
	
	/**
	 * 
	 * @return formált highScore : {@link String}
	 */
	public String getScore() {
		return highScore.formatScore();
	}
	
	/**
	 * 
	 * @return camera : {@link Camera}
	 */
	public Camera getCamera() {
		return camera;
	}
	
	/**
	 * 
	 * @return chapterHighScoreManager : {@link ChapterHighScoreManager}
	 */
	public ChapterHighScoreManager getChapterHighScoreManager() {
		return chapterHighScoreManager;
	}
}
