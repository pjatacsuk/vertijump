package com.vjmp.gfx;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.vjmp.Player;

/**
 * A Player animációit tartalmazó osztály. Felelõs ezen animációk frissitéséért a {@link PlayerState} függvénéyben. <br>
 * 
 * A Player sprite-okat tartalmazó .png felépitése a következõ:<br>
 * sprite width = 45, offSet = 10 <br> 
 * standing sprite = 0. sprite <br>
 * jumping sprite = 5. sprite <br>
 * run right sprites = 1..2 sprites <br>
 * run left sprites = 2..4 sprites <br>
 */
public class PlayerSprite {
	
	public enum PlayerState{
			RUNNING_LEFT,RUNNING_RIGHT,JUMPING,STANDING
	};
	
	private AnimatedSprite running_right = null;
	private AnimatedSprite running_left = null;
	private Sprite standing = null;
	private Sprite jumping = null;
	
	private PlayerState playerState = PlayerState.STANDING;
	
	/**
	 * Konstruktor
	 * @param path : {@link String} - Az sprite-okat tartalmazó kép.
	 */
	public PlayerSprite(String path){
		Sprite tmp = new Sprite(path);
		standing = new Sprite(tmp.getSubImg(0,0,45,45),path);
		
		jumping = new Sprite(tmp.getSubImg(5*(45+10),0,45,45),path);
		
		running_right = new AnimatedSprite(tmp.getSubImg(45+10,0,(45+10)*2,45),
											45,
											10);
		running_left = new AnimatedSprite(tmp.getSubImg((45+10)*3,0,(45+10)*2,45),
										  45,
										  10);
		
		
	}
	
	/**
	 * Beállitja a {@link Player}-nek a  {@link PlayerState}-jét.
	 * @param playerState : {@link PlayerState} - a beállitandó érték
	 */
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}
	
	/**
	 * Eltmozgatja az animációkat.
	 * @param x : double - az x tengelyen való elmozgatás mértéke
	 * @param y : double - az y tengelyen való elmozgatás mértéke
	 */
	public void move(double x,double y){
		standing.move(x, y);
		jumping.move(x, y);
		running_right.move(x, y);
		running_left.move(x, y);
		
	}
	
	/**
	 * Frissiti az animációkat.
	 */
	public void update(){
		switch(playerState) {
		case STANDING:
		case JUMPING:
			break;
		case RUNNING_LEFT:
			running_right.reset();
			running_left.update();
			break;
		case RUNNING_RIGHT:
			running_left.reset();
			running_right.update();
			break;
			
		}
		
	}
	
	/**
	 * Kirajzolja az animációkat.
	 * @param g : {@link Graphics}
	 */
	public void draw(Graphics g){
		switch(playerState) {
		case STANDING:
			standing.draw(g);
		case JUMPING:
			jumping.draw(g);
			break;
		case RUNNING_LEFT:
			running_left.draw(g);
			break;
		case RUNNING_RIGHT:
			running_right.draw(g);
			break;
			
		}
		
	}
	
	/**
	 * Visszatér az álló ("standing") sprite {@link Rectangle}-jével
	 * @return rect : {@link Rectangle} - a standing sprite {@link Rectangle}-je
	 */
	public Rectangle getRect() {
		return standing.getRect();
	}
	
	/**
	 * A megadott pozicióra rakja az animációkat
	 * @param x : int - a megadott x pozició
	 * @param y : int - a megadott y pozició
	 */
	public void setLocation(int x, int y) {
		standing.setLocation(x, y);
		jumping.setLocation(x, y);
		running_left.setLocation(x,y);
		running_right.setLocation(x,y);
		
	}
	
	/**
	 * Visszatér a standing sprite x poziciójával
	 * @return x : int - a standing sprite x poziciója
	 */
	public int GetPosX() {
		return standing.GetPosX();
	}
	
	/**
	 * Visszatér a standing sprite y poziciójával
	 * @return y : int - a stanidng sprite y poziciója
	 */
	public int GetPosY() {
		return standing.GetPosY();
	}
	
	/**
	 * Visszatér a standing sprite {@link Rectangle}-jének szélességével
	 * @return width - int : a standing sprite {@link Rectangle}-jének szélessége
	 */
	public int getRectWidth() {
		return standing.getRectWidth();
	}
	
	
}
