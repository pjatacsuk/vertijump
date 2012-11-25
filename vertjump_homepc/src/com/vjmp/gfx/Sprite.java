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

import org.w3c.dom.css.Rect;

import com.vjmp.gfx.Sprite.SpriteType;

/**
 * A sprite egy olyan osztály, ami tartalmaz egy BufferedImage-t, majd ezen képet a sprite által meghatározott
 * pozicióra, rectanglre rajzolja ki.
 * 
 * 
 * @author User
 *
 */
public class Sprite implements Serializable {

	
	
	public enum Dir{NORTH,WEST,EAST,SOUTH};
	public enum SpriteType {SCALE,REPEAT,NORMAL};
	
	private static final long serialVersionUID = 1L;
	private int pos_x = 0;
	private int pos_y = 0;
	
	private BufferedImage img = null;
	private boolean[]	  wall = null;
	private boolean       isVisible = true;
	private Rectangle     rect = null;
	private boolean		  marked = false;
	private String		  path;
	private SpriteType 	  spriteType = SpriteType.SCALE;
	
	/**
	 * Konstruktor.
	 * 
	 * @param path : {@link String} - a sprite elérési útvonala 
	 * @param psx : int - a sprite x poziciója
	 * @param psy : int - a sprite y poziciója
	 * @param visibility : boolean - a sprite láthatósága
	 */
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
		wall[getDirIndex(Dir.NORTH)] = true;
		wall[getDirIndex(Dir.WEST)] = true;
		wall[getDirIndex(Dir.SOUTH)] = true;
		wall[getDirIndex(Dir.EAST)] = true;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param t_path : {@link String} - a sprite elérési útvonala
	 * @param t_rect : {@link Rectangle} - a sprite {@link Rectangle}-je
	 * @param visibility : boolean - a sprite láthatósága
	 */
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
		wall[getDirIndex(Dir.NORTH)] = true;
		wall[getDirIndex(Dir.WEST)] = true;
		wall[getDirIndex(Dir.SOUTH)] = true;
		wall[getDirIndex(Dir.EAST)] = true;
	}
	
	/**
	 * Konstruktor.
	 * 
	 * @param t_path : {@link String} - a sprite elérési útvonala
	 * @param t_rect : {@link Rectangle} - a sprite {@link Rectangle}-je
	 * @param visibility : boolean - a sprite láthatósága
	 * @param wall : boolean[] - a sprite falai
	 * @param spriteType : {@link SpriteType} - a sprite tipusa
	 */
	public Sprite(String t_path,Rectangle t_rect,boolean visibility,boolean[] wall, SpriteType spriteType) {
		img = null;
		this.path = t_path;
		this.rect = t_rect;
		this.spriteType = spriteType;
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
		
		if(spriteType == SpriteType.REPEAT){
			int width_count = rect.width/img.getWidth() +1;
			int height_count = rect.height/img.getHeight() +1;
		
			rect = new Rectangle(rect.x,rect.y,width_count*img.getWidth(),height_count*img.getHeight());
		
		} else if(spriteType == SpriteType.NORMAL) {
			rect = new Rectangle(rect.x,rect.y,img.getWidth(),img.getHeight());
		}
	}
	
	/**
	 * 
	 * @param dir : {@link Dir} - megadja a dir által meghatározott indexet
	 * @return ret : int - index
	 */
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
	
	/**
	 * Megadja a {@link Dir} által meghatározott fal létezését.
	 * @param dir : {@link Dir} -  a vizsgált fal
	 * @return boolean : true - ha van ott val, false - ha nincs
	 */
	public boolean GetWall(Dir dir) {
		return wall[getDirIndex(dir)];
	}
	
	/**
	 * 
	 * A megadott elérési útvonal alapján betölt egy képet, majd azt a megfelelõ méretûre transzformálja
	 * a megadott rectangle alapján.
	 * @deprecated
	 * @param t_path : String - a sprite elérési útvonala
	 * @param t_rect : {@link Rectangle} - a megadott {@link Rectangle}
 	 */
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
	
	/**
	 * Betölt egy képet a megadott string alapján.
	 * @param string : {@link String} - a kép elérési útvonala 
	 */
	public Sprite(String string) {
		path = string;
		img = null;
		try {
			img = ImageIO.read(new File(string));
		} catch (IOException e) {
			System.out.println("No image for you!");
		}
		rect = new Rectangle(pos_x,pos_y,img.getWidth(),img.getHeight());
	}

	/**
	 * Lemásolja a megadott képet, majd elmenti a megadott útvonalat.
	 * @param img : {@link BufferedImage} - a megadott kép
	 * @param path : {@link String} - a megadott útvonal
	 */
	public Sprite(BufferedImage img,String path) {
		this.img = img;
		this.path = path;
		
		rect = new Rectangle(pos_x,pos_y,img.getWidth(),img.getHeight());
		wall = new boolean[4];
		wall[getDirIndex(Dir.NORTH)] = false;
		wall[getDirIndex(Dir.WEST)] = false;
		wall[getDirIndex(Dir.SOUTH)] = false;
		wall[getDirIndex(Dir.EAST)] = false;
		isVisible = true;
	}
	
	/**
	 * Kirajzolja a sprite-ot a {@link SpriteType} alapján.
	 * @param g : {@link Graphics}
	 */
	public void draw(Graphics g) {
		if(img != null){
			if(isVisible) { 
				
				switch(spriteType) {
				case SCALE:
					g.drawImage(img,pos_x,pos_y,rect.width,rect.height,null);
				break;
				case NORMAL:
					g.drawImage(img, pos_x, pos_y,rect.width,rect.height,null);	
				break;
				case REPEAT:
					drawRepeatedImage(g);
					break;
				}
			}
		} else {
			//todo exceptionok
		}
	}
	
	/**
	 * Kirajzolja a rectangle-be ismételten a képet amig belefér.
	 * @param g : {@link Graphics}
	 */
	private void drawRepeatedImage(Graphics g) {
		int width_count = rect.width/img.getWidth();
		int height_count = rect.height/img.getHeight();
		
		for(int y=0;y<height_count;y++){
			for(int x=0;x<width_count;x++){
				g.drawImage(img,rect.x + x*img.getWidth(),rect.y + y*img.getHeight(),null );
			}
		
		}
		
	}
	
	/**
	 * Eltolja a sprite-ot az x,y tengelyen.
	 * @param x : double - az eltolás x mértéke
	 * @param y : double - az eltolás y mértéke
	 */
	public void move(double x,double y) {
		pos_x += x;
		pos_y += y;
		rect.setLocation(pos_x,pos_y);
		
	}
	
	/**
	 * Visszaadja a sprite X pozicióját.
	 * @return pos_x : int - a sprite X poziciója
	 */
	public int GetPosX() {
		return pos_x;
	}
	
	/**
	 * Visszaadja a sprite Y pozicióját.
	 * @return pos_y : int - a sprite Y poziciója
	 */
	public int GetPosY() {
		return pos_y;
	}

	/**
	 * Visszaadja a sprite láthatóságát.
	 * @return true - ha látható a sprite, false - ha nem látható
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Beállitja a sprite láthatóságát a megadott paraméter alapján.
	 * @param isVisible : boolean - a megadott paraméter
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	/**
	 * Visszaadja a sprite {@link Rectangle}-jét.
	 * @return rect : {@link Rectangle} - a sprite {@link Rectangle}-je 
	 */
	public Rectangle getRect() {
		return rect;
	}

	/**
	 * Visszaadja a {@link Rectangle} szélességét.
	 * @return width : int - A {@link Rectangle} szélessége
	 */
	public int getRectWidth() {
		return rect.width;
	}
	
	/**
	 * Visszaadja a {@link Rectangle} magasságát.
	 * @return height : int - a {@link Rectangle} magassága
	 */
	public int getRectHeight() {
		return rect.height;
	}
	
	/**
	 * Serializáció.
	 * 
	 * @param stream : {@link ObjectOutputStream}
	 * @throws IOException
	 */
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		
		stream.writeUTF(path);
		stream.writeObject(rect);
		stream.writeBoolean(isVisible);
		stream.writeObject(spriteType);
		for(int i=0;i<4;i++) {
			stream.writeBoolean(wall[i]);
		}
		
		 
	 }
	 
	 /**
	  * Deserializáció.
	  * @param in : {@link ObjectInputStream}
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		 this.path= in.readUTF();
		 this.rect = (Rectangle)in.readObject();
		 this.isVisible = in.readBoolean();
		 this.spriteType =(SpriteType)in.readObject();
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
	 
	 /**
	  * Beállitja a sprite pozicióját a megadott helyre.
	  * @param x : int - a megadott x pozició
	  * @param y : int - a megadott y pozició
	  */
	 public void setLocation(int x,int y) {
		 pos_x = x;
		 pos_y = y;
		 rect.setLocation(x,y);
	 }

	 /**
	  * Visszatér a sprite poziciójával.
	  * @return point : {@link Point} - a sprite poziciója
	  */
	public Point GetLocation() {
		return new Point(pos_x,pos_y);
	}
	
	/**
	 * Visszatér a sprite elérési útvonalával.
	 * @return path : {@link String} - a sprite elérési útvonala
	 */
	public String GetPath() {
		return path;
	}
	
	/**
	 * Beállitja a sprite láthatóságát.
	 * @param visibility : boolean - beállitandó érték
	 */
	public void setVisibility(boolean visibility) {
		isVisible = visibility;
	}
	
	/**
	 * Visszatér a {@link BufferedImage} magasságával.
	 * @return height : int - a {@link BufferedImage} magassága
	 */
	public int getImgHeight() {
		return img.getHeight();
	}
	
	/**
	 * Visszatér a {@link BufferedImage} szélességével.
	 * @return width : int - a BufferedImage szélessége
	 */
	public int getImgWidth() {
		return img.getWidth();
	}
	
	/**
	 * Visszatér a {@link BufferedImage} egy részletével a megadott paraméterek alapján.
	 * @param x : int - a kivágandó kép x poziciója
	 * @param y : int - a kivágandó kép y poziciója
	 * @param w : int - a kivágandó kép szeléssége
	 * @param h : int - a kivágandó kép magassága
	 * @return ret : {@link BufferedImage} - a kivágott kép
	 */
	public BufferedImage getSubImg(int x,int y,int w,int h){
		return img.getSubimage(x, y, w, h);
	}
	
	/**
	 * Beállitja a sprite tipusát a megadott {@link SpriteType} alapján.
	 * 
	 * @param spriteType
	 */
	public void setSpriteType(SpriteType spriteType) {
		this.spriteType = spriteType;
		
	}


}