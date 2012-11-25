package com.vjmp.gfx;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.vjmp.Player;

/**
 * A Player anim�ci�it tartalmaz� oszt�ly. Felel�s ezen anim�ci�k frissit�s��rt a {@link PlayerState} f�ggv�n�yben. <br>
 * 
 * A Player sprite-okat tartalmaz� .png fel�pit�se a k�vetkez�:<br>
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
	 * @param path : {@link String} - Az sprite-okat tartalmaz� k�p.
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
	 * Be�llitja a {@link Player}-nek a  {@link PlayerState}-j�t.
	 * @param playerState : {@link PlayerState} - a be�llitand� �rt�k
	 */
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}
	
	/**
	 * Eltmozgatja az anim�ci�kat.
	 * @param x : double - az x tengelyen val� elmozgat�s m�rt�ke
	 * @param y : double - az y tengelyen val� elmozgat�s m�rt�ke
	 */
	public void move(double x,double y){
		standing.move(x, y);
		jumping.move(x, y);
		running_right.move(x, y);
		running_left.move(x, y);
		
	}
	
	/**
	 * Frissiti az anim�ci�kat.
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
	 * Kirajzolja az anim�ci�kat.
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
	 * Visszat�r az �ll� ("standing") sprite {@link Rectangle}-j�vel
	 * @return rect : {@link Rectangle} - a standing sprite {@link Rectangle}-je
	 */
	public Rectangle getRect() {
		return standing.getRect();
	}
	
	/**
	 * A megadott pozici�ra rakja az anim�ci�kat
	 * @param x : int - a megadott x pozici�
	 * @param y : int - a megadott y pozici�
	 */
	public void setLocation(int x, int y) {
		standing.setLocation(x, y);
		jumping.setLocation(x, y);
		running_left.setLocation(x,y);
		running_right.setLocation(x,y);
		
	}
	
	/**
	 * Visszat�r a standing sprite x pozici�j�val
	 * @return x : int - a standing sprite x pozici�ja
	 */
	public int GetPosX() {
		return standing.GetPosX();
	}
	
	/**
	 * Visszat�r a standing sprite y pozici�j�val
	 * @return y : int - a stanidng sprite y pozici�ja
	 */
	public int GetPosY() {
		return standing.GetPosY();
	}
	
	/**
	 * Visszat�r a standing sprite {@link Rectangle}-j�nek sz�less�g�vel
	 * @return width - int : a standing sprite {@link Rectangle}-j�nek sz�less�ge
	 */
	public int getRectWidth() {
		return standing.getRectWidth();
	}
	
	
}
