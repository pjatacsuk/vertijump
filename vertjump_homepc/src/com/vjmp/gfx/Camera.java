package com.vjmp.gfx;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import com.vjmp.Game;
import com.vjmp.InputHandler;
import com.vjmp.Player;
import com.vjmp.editor.Editor;

/**
 * A Camera osztály felelõs a nézeti transzformációért. {@link Editor} esetén a camera WASD billentyûkkel mozgatható,
 * {@link Game} estén a kamera a player mozgását követve felelé mozog, lefelé már nem. Ez játék mechanika miatt van igy.
 * 
 * @author User
 *
 */
public class Camera {
	public int pos_x=0;
	public int pos_y=0;
	
	public int width=0;
	public int height=0;
	private int editor_speed = 15;
	private AffineTransform old_transform;
	private double zoom=1;
	/**
	 * Konstuktor
	 * @param width : int - a {@link Camera} képének szélessége
	 * @param height : int - a {@link Camera} képének magassága
	 */
	public Camera(int width,int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Frissiti a {@link Camera} helyezét a {@link Player} helyzetének függvényében
	 * @param player_x : int - a {@link Player} x poziciója
	 * @param player_y : int - a {@link Player} y poziciója
	 */
	public void update(int player_x,int player_y) {
		
		int current_player_y = player_y + pos_y;
		if(current_player_y < (double)(height / 3)) {
			pos_y += height/3 - current_player_y;
		}
	}
	
	/**
	 * Frissiti a {@link Camera} helyzetét a WASD billentyûk lenyomásának függvényében
	 * @param inputHandler - {@link InputHandler} : az input kezelése
	 */
	public void update(InputHandler inputHandler) {
		if(inputHandler.W.isPressed()) {
			pos_y += editor_speed;
		
		}
		if(inputHandler.A.isPressed()) {
		   pos_x += editor_speed;
	
		}
		if(inputHandler.S.isPressed()) {
			pos_y -= editor_speed;
			
		}
		if(inputHandler.D.isPressed()) {
			pos_x -= editor_speed;
			
		}
		
	}
	
	/**
	 * Visszatér az ez elõtti transzofrámcióval
	 * @return old_transform : {@link AffineTransform}
	 */
	public AffineTransform GetOldTransform() {
		return old_transform;
	}
	
	/**
	 * Transzformálja a "g" által megadott transzformációt a camera által szolgáltott pozicióval,
	 * az elõzõ transzformációt elmenti
	 * @param g : {@link Graphics2D}
	 * @return tr2 : {@link AffineTransform} - a transzformált camera kép
	 */
	public AffineTransform GetTransform(Graphics2D g) {
		
		 old_transform = g.getTransform();
		 AffineTransform tr2= new AffineTransform(old_transform);
		 tr2.scale(zoom,zoom);
		 tr2.translate(pos_x, pos_y);
		 
		 return tr2;
	}
}
