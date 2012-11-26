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

/**
 * 
 * A játékos mozgását, megjelenitését, frissitését végzõ osztály.
 * A játékos figura kollozióját, ugrás mechanikáját és végzi.
 * 
 */
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

	
	/**
	 * Konstruktor
	 * @param iHandler : {@link InputHandler}
	 */
	public Player(InputHandler iHandler) {
		playerSprite = new PlayerSprite("./res/spooky_anim.png");
		inputHandler = iHandler;
		playerSprite.move(130, 500);
		sampleManager = new SampleManager();
		sampleManager.add(new Sample("jump","./res/sound/jump.wav"));
	}

	/**
	 * A jatekos frissitését végzi. Kezeli az inputot, a kollóziót, a poziciót, 
	 * a játékos logikáját és az animált spriteot.
	 * 
	 * @param map : {@link Map}
	 */
	public void update(Map map) {
		HandleInput();
		UpdateVelocity();

		MovePlayer();

		CheckCollosion(map);
		StickyUpdate();
		UpdatePlayerSprite();
		
	}

	/**
	 * Updateli a játékos sprite-ját a mozgásnak megfelelõen
	 */
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

	/**
	 * Updateli a sticky mechanizmust a sebességnek megfelelõen
	 */
	private void StickyUpdate() {
		if (Math.abs(speed_x) > 0.01) {
			sticky = false;
			stick_dir = STICK_DIR.NONE;
		}
		
	}

	/**
	 * A collosion detection-t végzi a függvény, collosion esetén meghivja a collosiot
	 * kezelõ függvényt
	 * @param map : {@link Map}
	 */
	private synchronized void CheckCollosion(Map map) {
	
		//elõszõr a trigger-eken futunk végig és kezeljük õket
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
					if (s.isVisible()) {
						Collosion(s);
					}
				}
		}

		

	}

	/**
	 * A {@link TriggerEntity} collosion-t kezeli
	 * @param triggerEntity : {@link TriggerEntity} - a megfelelõ trigger mechanizmust aktiválja 
	 * ha ütközés van
	 */
	private void TriggerCollosion(TriggerEntity triggerEntity) {
		Rectangle rect = triggerEntity.getRect();
		Rectangle intersection = playerSprite.getRect().intersection(rect);
		if(intersection.width+5 > intersection.height) {
			//a +5 magic number azert kell, mert jelenlegi meretek mellet
			//ha nagyon kicsi az intersection kepes felre ertelmezni a feltetelt
			//kesobb fixalasra jogosult, ha nem mukodik
			
			// y tengely menti ütközések
			
			if(speed_y > 0 && triggerEntity.getWall(Dir.NORTH)) {
				triggerEntity.activateTrigger();
			}
			else if(triggerEntity.getWall(Dir.SOUTH)){
				triggerEntity.activateTrigger();
			}  
		} else {	
			//x tengely menti ütközések
			if(speed_x > 0 && triggerEntity.getWall(Dir.WEST)) {
				triggerEntity.activateTrigger();
			}
			else  if(speed_x < 0 && triggerEntity.getWall(Dir.EAST)){
				triggerEntity.activateTrigger();
		    }
		}		
		
	}

	
	/**
	 * A sima {@link DrawableEntity} collosion-t kezeli 
	 * @param drawableEntity : {@link DrawableEntity} - az az entity amivel ütköztünk
	 */
	private void Collosion(DrawableEntity drawableEntity) {
		Rectangle rect = drawableEntity.getRect();
		Rectangle intersection = playerSprite.getRect().intersection(rect);
		if (intersection.width + 5 > intersection.height) {
			// a +5 magic number azert kell, mert jelenlegi meretek mellet
			// ha nagyon kicsi az intersection kepes felre ertelmezni a
			// feltetelt
			// kesobb fixalasra jogosult, ha nem mukodik

			//y tengely menti ütközések
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
			//x tengely menti ütközések
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

	/**
	 * A játékos gyorsulását frissiti.
	 */
	private void UpdateVelocity() {
		// a velocity nem haladhat meg egy kuszoberteket

		// irannyal ellentetesen csokkenteni kell a velocityt
		if (velocity_x > 0 && !inputHandler.D.isPressed()) {
			velocity_x -= velocity_change;

		} else if (velocity_x < 0 && !inputHandler.A.isPressed()) {
			velocity_x += velocity_change;

		}	

		if (jumping) {
			
			velocity_y += -jump_speed;
			if (Math.abs(velocity_y) > max_velocity_y) {
				//rá állitjuk a küszöbértékre
				velocity_y = max_velocity_y	* ((velocity_y) / Math.abs(velocity_y));
			}
		}

		//gravitáció
		velocity_y += velocity_gravity;

		if (speed_y < -max_speed_y) {
			speed_y = max_speed_y * ((speed_y) / Math.abs(speed_y));
			
			//ha elértük a maximális ugrási sebességet, többé nem számit a W lenyomása
			ignore_W = true;
			jumping = false;
		}
		if (Math.abs(velocity_x) >= max_velocity_x) {
			velocity_x = max_velocity_x * ((velocity_x) / Math.abs(velocity_x));
		}
		if (sticky) {
			//há ráragadtunk a falra csak kicsit csúszunk lefelé
			velocity_y = 1;
			speed_y = 1;
			ignore_W = false;
		}
	}

	/**
	 * A játékos sebességét frissiti.
	 * 
	 */
	private void ChangePlayerSpeed() {
		if (velocity_x != 0) {
			if (Math.abs(speed_x) <= max_speed_x) {
				//1/60-al kell változtani a sebességet a framerate miatt
				speed_x += velocity_x * (1.0 / 60.0);
			}
		} else {
			//a tényleges sebességet számoljuk ki, küszöbérték alatt 0.
			if (Math.abs(speed_x) >= 0.2) {
				int dir = 1;
				if (speed_x > 0) {
					dir = 1;
				} else {
					
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

	/**
	 * Beállitja a játékos kivánt pozicióját 
	 * @param p : {@link Point} - a kivánt pozició
	 */
	public void setLocation(Point p) {
		playerSprite.setLocation(p.x, p.y);
	}

	/**
	 * A játékos mozgatását végzi, ha kifutunk a képernyõrõl a másik oldalt
	 * térünk vissza.
	 */
	private void MovePlayer() {
		ChangePlayerSpeed();

		playerSprite.move(speed_x, speed_y);
		//Az egyik oldalon ki, másikon be mechanizmus
		if (playerSprite.GetPosX() + playerSprite.getRectWidth() < 0) {
			playerSprite.move(180 * 3, 0);
		}
		if (playerSprite.GetPosX() > 180 * 3) {
			playerSprite.move(-180 * 3, 0);
		}

	}

	/**
	 * Az input-okat dolgozza fel.
	 */
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
			// a sprintelés mechanizmusa, TODO: refactor metódussá
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

	/**
	 * A játékost rajzolja ki.
	 * @param g : {@link Graphics}
	 */
	public void draw(Graphics g) {
		playerSprite.draw(g);
	}

	/**
	 *  A játékos X koordinátáját adja vissza
	 * @return x : int - a játékos x koordinátája
	 */
	public int GetPosX() {
		return playerSprite.GetPosX();
	}

	/**
	 *  A játékos Y koordinátáját adja vissza
	 * @return y : int - a játékos y koordinátája
	 */
	public int GetPosY() {
		return playerSprite.GetPosY();
	}
}
