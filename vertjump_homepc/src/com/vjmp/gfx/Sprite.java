package com.vjmp.gfx;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	private int pos_x = 0;
	private int pos_y = 0;
	
	private BufferedImage img = null;
	private boolean       isVisible = true;
	private Rectangle     rect = null;
	private boolean		  marked = false;
	
	public Sprite(String path,int psx,int psy, boolean visibility) {
		img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
		
		pos_x = psx;
		pos_y = psy;
		rect = new Rectangle(pos_x,pos_y,img.getWidth(),img.getHeight());
		isVisible = visibility;
		
	}
	
	public Sprite(String string) {
		img = null;
		try {
			img = ImageIO.read(new File(string));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
		rect = new Rectangle(pos_x,pos_y,img.getWidth(),img.getHeight());
	}

	public void draw(Graphics g) {
		if(isVisible) {
		g.drawImage(img,pos_x,pos_y,null);

		}
	}
	
	public void move(double x,double y) {
		pos_x += x;
		pos_y += y;
		rect.setLocation(pos_x,pos_y);
		
	}
	
	public int GetPosX() {
		return pos_x;
	}
	public int GetPosY() {
		return pos_y;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public Rectangle getRect() {
		return rect;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public int GetWidth() {
		return img.getWidth();
	}
	public int GetHeight() {
		return img.getHeight();
	}





}