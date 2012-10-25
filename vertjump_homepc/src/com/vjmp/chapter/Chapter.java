package com.vjmp.chapter;

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

public class Chapter {
	private Map map = null;
	private Camera camera = null;
	private Player player = null;
	private InputHandler inputHandler = null;
	private static BufferedImage bg;
	private int WIDTH = 0;
	private int HEIGHT = 0;
	
	public Chapter(InputHandler inputHandler,int WIDTH,int HEIGHT,String map_path) {
			player = new Player(inputHandler);
			camera = new Camera(WIDTH,HEIGHT);
			map = new Map(WIDTH,HEIGHT,true);
			
			this.WIDTH = WIDTH;
			this.HEIGHT = HEIGHT;
			this.inputHandler = inputHandler;
			load_resource();
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
			 ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			 Editor editor = (Editor)ois.readObject();
			 map = new Map(editor.getMap(),false);
			 
			 player.setLocation(editor.GetPlayerLocation());
			 camera.pos_y =0;
			 ois.close();
	 }
	 
	 public void update() {
		 	player.update(map);
			camera.update(player.GetPosX(), player.GetPosY());
			map.update(camera);
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
			g.dispose();
	 }
	
}
