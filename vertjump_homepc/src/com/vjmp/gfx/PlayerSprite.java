package com.vjmp.gfx;

import java.awt.Graphics;
import java.awt.Rectangle;

public class PlayerSprite {
	/*A Player sprite-okat tartalmazó .png felépitése a következõ:
	 * sprite width = 45, offSet = 10 
	 * standing sprite = 0. sprite
	 * jumping sprite = 5. sprite
	 * run right sprites = 1..2 sprites
	 * run left sprites = 2..4 sprites
	 */
	public enum PlayerState{
			RUNNING_LEFT,RUNNING_RIGHT,JUMPING,STANDING
	};
	
	private AnimatedSprite running_right = null;
	private AnimatedSprite running_left = null;
	private Sprite standing = null;
	private Sprite jumping = null;
	
	private PlayerState playerState = PlayerState.STANDING;
	
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
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}
	public void move(double x,double y){
		standing.move(x, y);
		jumping.move(x, y);
		running_right.move(x, y);
		running_left.move(x, y);
		
	}
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
	public Rectangle getRect() {
		return standing.getRect();
	}
	public void setLocation(int x, int y) {
		standing.setLocation(x, y);
		jumping.setLocation(x, y);
		running_left.setLocation(x,y);
		running_right.setLocation(x,y);
		
	}
	public int GetPosX() {
		return standing.GetPosX();
	}
	public int GetPosY() {
		return standing.GetPosY();
	}
	public int getRectWidth() {
		return standing.getRectWidth();
	}
	
	
}
