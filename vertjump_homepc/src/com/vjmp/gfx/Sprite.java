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
 * A sprite egy olyan oszt�ly, ami tartalmaz egy BufferedImage-t, majd ezen k�pet a sprite �ltal meghat�rozott
 * pozici�ra, rectanglre rajzolja ki.
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
	 * @param path : {@link String} - a sprite el�r�si �tvonala 
	 * @param psx : int - a sprite x pozici�ja
	 * @param psy : int - a sprite y pozici�ja
	 * @param visibility : boolean - a sprite l�that�s�ga
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
	 * @param t_path : {@link String} - a sprite el�r�si �tvonala
	 * @param t_rect : {@link Rectangle} - a sprite {@link Rectangle}-je
	 * @param visibility : boolean - a sprite l�that�s�ga
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
	 * @param t_path : {@link String} - a sprite el�r�si �tvonala
	 * @param t_rect : {@link Rectangle} - a sprite {@link Rectangle}-je
	 * @param visibility : boolean - a sprite l�that�s�ga
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
	 * @param dir : {@link Dir} - megadja a dir �ltal meghat�rozott indexet
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
	 * Megadja a {@link Dir} �ltal meghat�rozott fal l�tez�s�t.
	 * @param dir : {@link Dir} -  a vizsg�lt fal
	 * @return boolean : true - ha van ott val, false - ha nincs
	 */
	public boolean GetWall(Dir dir) {
		return wall[getDirIndex(dir)];
	}
	
	/**
	 * 
	 * A megadott el�r�si �tvonal alapj�n bet�lt egy k�pet, majd azt a megfelel� m�ret�re transzform�lja
	 * a megadott rectangle alapj�n.
	 * @deprecated
	 * @param t_path : String - a sprite el�r�si �tvonala
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
	 * Bet�lt egy k�pet a megadott string alapj�n.
	 * @param string : {@link String} - a k�p el�r�si �tvonala 
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
	 * Lem�solja a megadott k�pet, majd elmenti a megadott �tvonalat.
	 * @param img : {@link BufferedImage} - a megadott k�p
	 * @param path : {@link String} - a megadott �tvonal
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
	 * Kirajzolja a sprite-ot a {@link SpriteType} alapj�n.
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
	 * Kirajzolja a rectangle-be ism�telten a k�pet amig belef�r.
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
	 * @param x : double - az eltol�s x m�rt�ke
	 * @param y : double - az eltol�s y m�rt�ke
	 */
	public void move(double x,double y) {
		pos_x += x;
		pos_y += y;
		rect.setLocation(pos_x,pos_y);
		
	}
	
	/**
	 * Visszaadja a sprite X pozici�j�t.
	 * @return pos_x : int - a sprite X pozici�ja
	 */
	public int GetPosX() {
		return pos_x;
	}
	
	/**
	 * Visszaadja a sprite Y pozici�j�t.
	 * @return pos_y : int - a sprite Y pozici�ja
	 */
	public int GetPosY() {
		return pos_y;
	}

	/**
	 * Visszaadja a sprite l�that�s�g�t.
	 * @return true - ha l�that� a sprite, false - ha nem l�that�
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Be�llitja a sprite l�that�s�g�t a megadott param�ter alapj�n.
	 * @param isVisible : boolean - a megadott param�ter
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	/**
	 * Visszaadja a sprite {@link Rectangle}-j�t.
	 * @return rect : {@link Rectangle} - a sprite {@link Rectangle}-je 
	 */
	public Rectangle getRect() {
		return rect;
	}

	/**
	 * Visszaadja a {@link Rectangle} sz�less�g�t.
	 * @return width : int - A {@link Rectangle} sz�less�ge
	 */
	public int getRectWidth() {
		return rect.width;
	}
	
	/**
	 * Visszaadja a {@link Rectangle} magass�g�t.
	 * @return height : int - a {@link Rectangle} magass�ga
	 */
	public int getRectHeight() {
		return rect.height;
	}
	
	/**
	 * Serializ�ci�.
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
	  * Deserializ�ci�.
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
	  * Be�llitja a sprite pozici�j�t a megadott helyre.
	  * @param x : int - a megadott x pozici�
	  * @param y : int - a megadott y pozici�
	  */
	 public void setLocation(int x,int y) {
		 pos_x = x;
		 pos_y = y;
		 rect.setLocation(x,y);
	 }

	 /**
	  * Visszat�r a sprite pozici�j�val.
	  * @return point : {@link Point} - a sprite pozici�ja
	  */
	public Point GetLocation() {
		return new Point(pos_x,pos_y);
	}
	
	/**
	 * Visszat�r a sprite el�r�si �tvonal�val.
	 * @return path : {@link String} - a sprite el�r�si �tvonala
	 */
	public String GetPath() {
		return path;
	}
	
	/**
	 * Be�llitja a sprite l�that�s�g�t.
	 * @param visibility : boolean - be�llitand� �rt�k
	 */
	public void setVisibility(boolean visibility) {
		isVisible = visibility;
	}
	
	/**
	 * Visszat�r a {@link BufferedImage} magass�g�val.
	 * @return height : int - a {@link BufferedImage} magass�ga
	 */
	public int getImgHeight() {
		return img.getHeight();
	}
	
	/**
	 * Visszat�r a {@link BufferedImage} sz�less�g�vel.
	 * @return width : int - a BufferedImage sz�less�ge
	 */
	public int getImgWidth() {
		return img.getWidth();
	}
	
	/**
	 * Visszat�r a {@link BufferedImage} egy r�szlet�vel a megadott param�terek alapj�n.
	 * @param x : int - a kiv�gand� k�p x pozici�ja
	 * @param y : int - a kiv�gand� k�p y pozici�ja
	 * @param w : int - a kiv�gand� k�p szel�ss�ge
	 * @param h : int - a kiv�gand� k�p magass�ga
	 * @return ret : {@link BufferedImage} - a kiv�gott k�p
	 */
	public BufferedImage getSubImg(int x,int y,int w,int h){
		return img.getSubimage(x, y, w, h);
	}
	
	/**
	 * Be�llitja a sprite tipus�t a megadott {@link SpriteType} alapj�n.
	 * 
	 * @param spriteType
	 */
	public void setSpriteType(SpriteType spriteType) {
		this.spriteType = spriteType;
		
	}


}