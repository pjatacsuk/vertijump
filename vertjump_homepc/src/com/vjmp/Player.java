package com.vjmp;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.vjmp.entities.Entity.EntityType;
import com.vjmp.entities.drawable.DrawableEntity;
import com.vjmp.entities.drawable.TriggerEntity;
import com.vjmp.entities.drawable.TriggerEntity.TriggerType;
import com.vjmp.gfx.PlayerSprite;
import com.vjmp.gfx.PlayerSprite.PlayerState;
import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.Sprite.Dir;
import com.vjmp.managers.SampleManager;
import com.vjmp.sound.Sample;

public class Player {
	enum STICK_DIR {
		LEFT, RIGHT, NONE
	};

	private PlayerSprite playerSprite = null;
	private InputHandler inputHandler = null;
	private SampleManager sampleManager = null;

	private static final int max_velocity_x_default = 15;
	private static final int max_velocity_y_default = 20;
	private static final int max_speed_x_default = 20;
	private static final int max_speed_y_default = 5;
	private double velocity_x = 0;
	private double velocity_y = 0;
	private double velocity_gravity = 2;
	private double max_velocity_x = 15;
	private double max_velocity_y = 30;
	private double max_speed_x = 20;
	private double max_speed_y = 5;

	private double velocity_change = 5;
	private double speed_x = 0;
	private double speed_y = 0;
	private double speed_x_change = 0;
	private double speed_y_change = 0;

	private double jump_speed = 15;
	private boolean jumping = false;
	private boolean ignore_W = false;
	private boolean ignore_SHIFT = false;
	private boolean sticky = false;
	private STICK_DIR stick_dir = STICK_DIR.NONE;

	public Player(InputHandler iHandler) {
		playerSprite = new PlayerSprite("./res/spooky_anim.png");
		inputHandler = iHandler;
		playerSprite.move(130, 500);
		sampleManager = new SampleManager();
		sampleManager.add(new Sample("jump","./res/sound/jump.wav"));
	}

	public void update(Map map) {
		HandleInput();
		UpdateVelocity();

		MovePlayer();

		CheckCollosion(map);
		StickyUpdate();
		UpdatePlayerSprite();
		
	}

	private void UpdatePlayerSprite() {
		if(speed_x == 0 && speed_y >= 0) {
			playerSprite.setPlayerState(PlayerState.STANDING);
		} else if(speed_y<0) {
			playerSprite.setPlayerState(PlayerState.JUMPING);
		} else if(speed_x>0) {
			playerSprite.setPlayerState(PlayerState.RUNNING_RIGHT);
		} else if(speed_x<0) {
			playerSprite.setPlayerState(PlayerState.RUNNING_LEFT);
		} 
		playerSprite.update();
	}

	private void StickyUpdate() {
		if (Math.abs(speed_x) > 0.01) {
			sticky = false;
			stick_dir = STICK_DIR.NONE;
		}
		
	}

	private synchronized void CheckCollosion(Map map) {
	
		for (TriggerEntity t : map.getTriggerEntityManager()) {
			
			if (t.intersects(playerSprite.getRect())) {
				TriggerCollosion(t);
			} else {
				t.disactivateTrigger();
			}
		}
		for(int i=0;i<map.getVisibleEntitiesSize();i++) {
			DrawableEntity s = map.getVisibleEntity(i);
				if (s.intersects(playerSprite.getRect())) {
					if (s.getType() == EntityType.TRIGGER) {
						HandleTriggerPlaceHolder(s);
					} else if (s.isVisible()) {
						Collosion(s);
					}
				}
		}

		

	}

	private void TriggerCollosion(TriggerEntity triggerEntity) {
		Rectangle rect = triggerEntity.getRect();
		Rectangle intersection = playerSprite.getRect().intersection(rect);
		if(intersection.width+5 > intersection.height) {
			//a +5 magic number azert kell, mert jelenlegi meretek mellet
			//ha nagyon kicsi az intersection kepes felre ertelmezni a feltetelt
			//kesobb fixalasra jogosult, ha nem mukodik
			
			if(speed_y > 0 && triggerEntity.getWall(Dir.NORTH)) {
				triggerEntity.activateTrigger();
			}
			else if(triggerEntity.getWall(Dir.SOUTH)){
				triggerEntity.activateTrigger();
			}  
		} else {	
			if(speed_x > 0 && triggerEntity.getWall(Dir.WEST)) {
				triggerEntity.activateTrigger();
			}
			else  if(speed_x < 0 && triggerEntity.getWall(Dir.EAST)){
				triggerEntity.activateTrigger();
		    }
		}		
		
	}

	private void HandleTriggerPlaceHolder(DrawableEntity s) {
		TriggerEntity tmp = (TriggerEntity) s;
		if (tmp.getTriggerType() == TriggerType.FINISH_LINE) {
			System.out.println("Finish!!!");
		}
	}

	private void Collosion(DrawableEntity drawableEntity) {
		Rectangle rect = drawableEntity.getRect();
		Rectangle intersection = playerSprite.getRect().intersection(rect);
		if (intersection.width + 5 > intersection.height) {
			// a +5 magic number azert kell, mert jelenlegi meretek mellet
			// ha nagyon kicsi az intersection kepes felre ertelmezni a
			// feltetelt
			// kesobb fixalasra jogosult, ha nem mukodik

			if (speed_y > 0 && drawableEntity.getWall(Dir.NORTH)) {
				playerSprite.move(0, -intersection.height);
				velocity_y = 0;
				speed_y = 0;
				ignore_W = false;
			} else if (drawableEntity.getWall(Dir.SOUTH)) {
				playerSprite.move(0, intersection.height);
				velocity_y = 0;
				speed_y = 0;

			}

		} else {

			if (speed_x > 0 && drawableEntity.getWall(Dir.WEST)) {

				sticky = true;
				stick_dir = STICK_DIR.LEFT;

				playerSprite.move(-intersection.width, 0);
				velocity_x = 0;
				speed_x = 0;
			} else if (speed_x < 0 && drawableEntity.getWall(Dir.EAST)) {

				sticky = true;
				stick_dir = STICK_DIR.RIGHT;

				playerSprite.move(intersection.width, 0);
				velocity_x = 0;
				speed_x = 0;
			}
		}

	}

	private void UpdateVelocity() {
		// a velocity nem haladhat meg egy kuszoberteket

		// irannyal ellentetesen csokkenteni kell a velocityt
		if (velocity_x > 0 && !inputHandler.D.isPressed()) {
			velocity_x -= velocity_change;

		} else if (velocity_x < 0 && !inputHandler.A.isPressed()) {
			velocity_x += velocity_change;

		}

		

		// gravity

		if (jumping) {
			
			velocity_y += -jump_speed;
			if (Math.abs(velocity_y) > max_velocity_y) {
				velocity_y = max_velocity_y
						* ((velocity_y) / Math.abs(velocity_y));
			}
		}

		velocity_y += velocity_gravity;

		if (speed_y < -max_speed_y) {
			speed_y = max_speed_y * ((speed_y) / Math.abs(speed_y));
			ignore_W = true;
			jumping = false;
		}
		if (Math.abs(velocity_x) >= max_velocity_x) {
			velocity_x = max_velocity_x * ((velocity_x) / Math.abs(velocity_x));
		}
		if (sticky) {
			velocity_y = 1;
			speed_y = 1;
			ignore_W = false;
		}
	}

	private void ChangePlayerSpeed() {
		if (velocity_x != 0) {
			if (Math.abs(speed_x) <= max_speed_x) {
				speed_x += velocity_x * (1.0 / 60.0);
			}
		} else {
			if (Math.abs(speed_x) >= 0.2) {
				int dir = 1;
				if (speed_x > 0) {
					dir = 1;
				} else {
					// speed < 0
					dir = -1;
				}

				if (speed_x_change == 0) {
					speed_x_change = Math.abs(speed_x * (1.0 / 20.0));
				}
				speed_x -= dir * speed_x_change;
			} else {
				speed_x_change = 0;
				speed_x = 0;
				
			}
		}
		if (velocity_y != 0) {
			if (Math.abs(speed_y) <= max_speed_y) {
				speed_y += velocity_y * (1.0 / 30.0);
			}
		}
	}

	public void setLocation(Point p) {
		playerSprite.setLocation(p.x, p.y);
	}

	private void MovePlayer() {

		

		ChangePlayerSpeed();

		playerSprite.move(speed_x, speed_y);
		if (playerSprite.GetPosX() + playerSprite.getRectWidth() < 0) {
			playerSprite.move(180 * 3, 0);
		}
		if (playerSprite.GetPosX() > 180 * 3) {
			playerSprite.move(-180 * 3, 0);
		}

	}

	public void HandleInput() {
		
		if (inputHandler.W.isPressed() && !ignore_W) {
			
			jumping = true;
			if(sticky != true){
				sampleManager.playSample("jump");
			}
			sticky = false; 
			

		} else if (!inputHandler.W.isPressed()) {
			
			jumping = false;
		}
		if (inputHandler.A.isPressed()) {

			// bug_fix, ha nem csinalnank,vegig lehetne csuzni a falon felfele
			// collosion miatt
			if (stick_dir != STICK_DIR.RIGHT) {
				velocity_x += -velocity_change;
				if (speed_x > 3)
					speed_x = 3;
				speed_x_change = 0;
			}
			playerSprite.setPlayerState(PlayerState.RUNNING_LEFT);

			
		}
		if (inputHandler.S.isPressed()) {
			

		}
		if (inputHandler.D.isPressed()) {

			// bug_fix, ha nem csinalnank,vegig lehetne csuzni a falon felfele
			// collosion miatt
			if (stick_dir != STICK_DIR.LEFT) {
				velocity_x += velocity_change;
				if (speed_x < -3)
					speed_x = -3;
				speed_x_change = 0;
			}

			playerSprite.setPlayerState(PlayerState.RUNNING_RIGHT);
		

		}
		if (inputHandler.SHIFT.isPressed() && !ignore_SHIFT) {

			max_velocity_x = max_velocity_x * 1.5;
			max_velocity_y = max_velocity_y * 1.5;
			max_speed_x = max_speed_x * 1.5;
			max_speed_y = max_speed_y * 1.5;
			ignore_SHIFT = true;
		} else if (!inputHandler.SHIFT.isPressed()) {
			max_speed_x = max_speed_x_default;
			max_speed_y = max_speed_y_default;
			ignore_SHIFT = false;
			max_velocity_x = max_velocity_x_default;
			max_velocity_y = max_velocity_y_default;
		}

	}

	public void draw(Graphics g) {
		playerSprite.draw(g);
	}

	public int GetPosX() {
		return playerSprite.GetPosX();
	}

	public int GetPosY() {
		return playerSprite.GetPosY();
	}
}
