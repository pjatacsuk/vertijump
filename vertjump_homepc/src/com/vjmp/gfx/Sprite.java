package com.vjmp.gfx;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Sprite implements Serializable {
	/**
	 * 
	 */
	public enum Dir{NORTH,WEST,EAST,SOUTH};
	
	private static final long serialVersionUID = 1L;
	private int pos_x = 0;
	private int pos_y = 0;
	
	private BufferedImage img = null;
	private boolean[]	  wall = null;
	private boolean       isVisible = true;
	private Rectangle     rect = null;
	private boolean		  marked = false;
	private String		  path;
	
	public Sprite(String path,int psx,int psy, boolean visibility) {
		img = null;
		this.path = path;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
		
		pos_x = psx;
		pos_y = psy;
		rect = new Rectangle(pos_x,pos_y,img.getWidth(),img.getHeight());
		isVisible = visibility;
		wall = new boolean[4];
		wall[getDirIndex(Dir.NORTH)] = false;
		wall[getDirIndex(Dir.WEST)] = false;
		wall[getDirIndex(Dir.SOUTH)] = false;
		wall[getDirIndex(Dir.EAST)] = false;
	}
	public Sprite(String t_path,Rectangle t_rect,boolean visibility) {
		img = null;
		this.path = t_path;
		this.rect = t_rect;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
		pos_x = rect.x;
		pos_y = rect.y;
		isVisible = visibility;
		wall = new boolean[4];
		wall[getDirIndex(Dir.NORTH)] = false;
		wall[getDirIndex(Dir.WEST)] = false;
		wall[getDirIndex(Dir.SOUTH)] = false;
		wall[getDirIndex(Dir.EAST)] = false;
	}
	public Sprite(String t_path,Rectangle t_rect,boolean visibility,boolean[] wall) {
		img = null;
		this.path = t_path;
		this.rect = t_rect;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
		pos_x = rect.x;
		pos_y = rect.y;
		isVisible = visibility;
		this.wall = new boolean[4];
		this.wall[getDirIndex(Dir.NORTH)] = wall[getDirIndex(Dir.NORTH)];
		this.wall[getDirIndex(Dir.WEST)] = wall[getDirIndex(Dir.WEST)];
		this.wall[getDirIndex(Dir.SOUTH)] = wall[getDirIndex(Dir.SOUTH)];
		this.wall[getDirIndex(Dir.EAST)] = wall[getDirIndex(Dir.EAST)];
	}
	public static int getDirIndex(Dir dir) {
		int ret = 0;
		switch(dir) {
		case NORTH:
			ret = 0;
			break;
		case WEST:
			ret = 1;
			break;
		case SOUTH:
			ret = 2;
			break;
		case EAST:
			ret = 3;
			break;
		}
		return ret;
	}
	public boolean GetWall(Dir dir) {
		return wall[getDirIndex(dir)];
	}
	public void TransformThisSpriteFromRect(String t_path,Rectangle t_rect) {
		img = null;
		this.path = t_path;
		this.rect = t_rect;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
		pos_x = rect.x;
		pos_y = rect.y;
		BufferedImage before = img;
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		int scale_x = rect.width / w;
		int scale_y = rect.height / h;
		at.scale(scale_x, scale_y);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before,null);
		img = after;
	
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

	public Sprite(Sprite sprite) {
		
	}
	public void draw(Graphics g) {
		if(img != null){
			if(isVisible) { 
			//	g.drawImage(img,pos_x,pos_y,null);
				g.drawImage(img,pos_x,pos_y,rect.width,rect.height,null);
	
			}
		} else {
			//todo exceptionok
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

	public int getRectWidth() {
		return rect.width;
	}
	public int getRectHeight() {
		return rect.height;
	}
	
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		stream.writeUTF(path);
		stream.writeObject(rect);
		stream.writeBoolean(isVisible);
		for(int i=0;i<4;i++) {
			stream.writeBoolean(wall[i]);
		}
		/*stream.writeInt(rect.x);
		stream.writeInt(rect.y);
		stream.writeInt(rect.width);
		stream.writeInt(rect.height);*/
		
		//	pw.close(); 
		 
	 }
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		 this.path= in.readUTF();
		 this.rect = (Rectangle)in.readObject();
		 this.isVisible = in.readBoolean();
		 wall = new boolean[4];
		 for(int i=0;i<4;i++) {
				wall[i] = in.readBoolean();
			} 
		 this.pos_x = rect.x;
		 this.pos_y = rect.y;
		 img = null;
			try {
				img = ImageIO.read(new File(path));
			} catch (IOException e) {
				System.out.println("No image for you!");
			}
		
			
	 }
	 public void setLocation(int x,int y) {
		 pos_x = x;
		 pos_y = y;
		 rect.setLocation(x,y);
	 }

	public Point GetLocation() {
		return new Point(pos_x,pos_y);
	}
	public String GetPath() {
		return path;
	}
	public void setVisibility(boolean visibility) {
		isVisible = visibility;
	}
	public int getImgHeight() {
		return img.getHeight();
	}
	public int getImgWidth() {
		return img.getWidth();
	}



}