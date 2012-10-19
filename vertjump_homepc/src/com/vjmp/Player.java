package com.vjmp;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.vjmp.gfx.Sprite;

public class Player {
	private Sprite sprite;
	private InputHandler inputHandler;
	
	private static final int max_velocity_x_default = 15;
	private static final int max_velocity_y_default = 20;
	private double velocity_x = 0;
	private double velocity_y = 0;
	private double velocity_gravity = 2;
	private double max_velocity_x = 15;
	private double max_velocity_y = 40;
	private double max_speed_x = 20;
	private double max_speed_y = 40;
	
	private double velocity_change =5 ;
	private double speed_x = 5;
	private double speed_y = 5;
	private double speed_x_change = 0;
	private double speed_y_change = 0;

	private double jump_speed = 15;
	private boolean jumping = false;
	private boolean ignore_W = false;

	
	
	public Player(InputHandler iHandler) {
		sprite = new Sprite("./res/spooky.png");
		inputHandler = iHandler;
		sprite.move(100, 500);
	}
	
	public void update(Map map) {
		HandleInput();
		UpdateVelocity();
		MovePlayer();
		CheckCollosion(map);
		if(velocity_x != 0 || velocity_y != 0)
		System.out.println(velocity_x + " " + velocity_y);
	}
	private void CheckCollosion(Map map) {
		for(Sprite s : map) {
			if(s.isVisible()) {
				if(sprite.getRect().intersects(s.getRect())) {
					Collosion(s.getRect());
				}
			}
		}
		
	}

	private void Collosion(Rectangle rect) {
		Rectangle intersection = sprite.getRect().intersection(rect);
		if(intersection.width+5 > intersection.height) {
			//a +5 magic number azert kell, mert jelenlegi meretek mellet
			//ha nagyon kicsi az intersection kepes felre ertelmezni a feltetelt
			//kesobb fixalasra jogosult, ha nem mukodik
			
			if(speed_y > 0) {
				sprite.move(0, -intersection.height);
				velocity_y = 0;
				speed_y = 0;
				ignore_W = false;
			}
			else {
				sprite.move(0, intersection.height);	
				velocity_y = 0;
				speed_y = 0;
				
			}
				
			
		} else {
		   if(speed_x > 0) {
			    sprite.move(-intersection.width,0);
		   		velocity_x = 0;
		   		speed_x = 0;
		   }
		   else {
			    sprite.move(intersection.width, 0);
		   		velocity_x = 0;
		   		speed_x = 0;
		   }
		}
		
		
	}

	private void UpdateVelocity() {
		//a velocity nem haladhat meg egy kuszoberteket
		
		
		//irannyal ellentetesen csokkenteni kell a velocityt
		if(velocity_x > 0 && !inputHandler.D.isPressed()) {
			velocity_x -= velocity_change;
			
		
		} else if(velocity_x < 0 && !inputHandler.A.isPressed()){
			velocity_x += velocity_change;
			
		}
	
/*		if(velocity_y > 0 && !inputHandler.S.isPressed()) {
			velocity_y -=velocity_change;
		} else if(velocity_y < 0 && !inputHandler.W.isPressed()){
			velocity_y +=velocity_change;
		} */
		
		//gravity
		
		
		
		if(jumping) {
			velocity_y +=  -jump_speed;
		} else {
			velocity_y += velocity_gravity;
		}
		if(Math.abs(velocity_y) >= max_velocity_y) {
			velocity_y = max_velocity_y * ((velocity_y) / Math.abs(velocity_y));
			ignore_W = true;
		}
		if(Math.abs(velocity_x) >= max_velocity_x) {
			velocity_x = max_velocity_x * ((velocity_x) / Math.abs(velocity_x));
		} 
		
	}
	
	private void ChangePlayerSpeed() {
		if(velocity_x != 0) {
			if(Math.abs(speed_x) <= max_speed_x) {
			speed_x += velocity_x * (1.0/60.0);
			}
		} else {
			if(Math.abs(speed_x) >= 0.2) {
				int dir=1;
				if(speed_x >0) {
					dir = 1;
				} else  {
					//speed < 0
					dir = -1;
				}
			
				if(speed_x_change == 0) {
					speed_x_change = Math.abs(speed_x * (1.0/20.0));
				}
				speed_x -= dir * speed_x_change;
			} else {
				speed_x_change = 0;
				speed_x = 0;
			}
		}
		if(velocity_y != 0) {
			if(speed_y <= max_speed_y) {
			speed_y += velocity_y * (1.0/60.0);
			}
		}
	}

	private void MovePlayer() {
		
		
	//	if(velocity_x == 0) speed_x =0;
	//	if(velocity_y == 0) speed_y =0;
//		System.out.println( velocity_x * (1.0/60.0) + " " +velocity_y * (1.0/60.0));
		
		ChangePlayerSpeed();
		
		sprite.move(speed_x, speed_y);
		if(sprite.GetPosX() + sprite.GetWidth() < 0) {
			sprite.move(180*3, 0);
		}
		if(sprite.GetPosX() > 180*3) {
			sprite.move(-180*3, 0);
		}
		
	}

	public void HandleInput() {
		//InputHandlerrel megnezzuk volt e gomb lenyomas es ha volt akkor kezeljuk
		
		if(inputHandler.W.isPressed() && !ignore_W) {
			jumping = true;
		//	velocity_y = 0;
		//	System.out.println("jumping");
			
		
		} else {
			jumping = false;
		//	System.out.println("not jumping");
		}
		if(inputHandler.A.isPressed()) {
			velocity_x += -velocity_change; 
			if(speed_x > 3) speed_x = 3;
			speed_x_change = 0;
		}
		if(inputHandler.S.isPressed()) {
			velocity_y += velocity_change;
			
		}
		if(inputHandler.D.isPressed()) {
			velocity_x += velocity_change;
			if(speed_x < -3)	speed_x = -3;
			speed_x_change = 0;
			
		}
	/*	if(inputHandler.SHIFT.isPressed()) {
			max_velocity_x  = max_velocity_x * 1.5;
			max_velocity_y  = max_velocity_y * 1.5;
		} else {
			max_velocity_x  = max_velocity_x_default;
			max_velocity_y  = max_velocity_x_default;
		}
		
	*/
	}
	
	public void draw(Graphics g) {
		sprite.draw(g);
	}
	public int GetPosX() {
		return sprite.GetPosX();
	}
	public int GetPosY() {
		return sprite.GetPosY();
	}
}
