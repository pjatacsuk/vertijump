package com.vjmp.gfx;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import com.vjmp.Game;
import com.vjmp.InputHandler;
import com.vjmp.Player;
import com.vjmp.editor.Editor;

/**
 * A Camera oszt�ly felel�s a n�zeti transzform�ci��rt. {@link Editor} eset�n a camera WASD billenty�kkel mozgathat�,
 * {@link Game} est�n a kamera a player mozg�s�t k�vetve felel� mozog, lefel� m�r nem. Ez j�t�k mechanika miatt van igy.
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
	 * @param width : int - a {@link Camera} k�p�nek sz�less�ge
	 * @param height : int - a {@link Camera} k�p�nek magass�ga
	 */
	public Camera(int width,int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Frissiti a {@link Camera} helyez�t a {@link Player} helyzet�nek f�ggv�ny�ben
	 * @param player_x : int - a {@link Player} x pozici�ja
	 * @param player_y : int - a {@link Player} y pozici�ja
	 */
	public void update(int player_x,int player_y) {
		
		int current_player_y = player_y + pos_y;
		if(current_player_y < (double)(height / 3)) {
			pos_y += height/3 - current_player_y;
		}
	}
	
	/**
	 * Frissiti a {@link Camera} helyzet�t a WASD billenty�k lenyom�s�nak f�ggv�ny�ben
	 * @param inputHandler - {@link InputHandler} : az input kezel�se
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
	 * Visszat�r az ez el�tti transzofr�mci�val
	 * @return old_transform : {@link AffineTransform}
	 */
	public AffineTransform GetOldTransform() {
		return old_transform;
	}
	
	/**
	 * Transzform�lja a "g" �ltal megadott transzform�ci�t a camera �ltal szolg�ltott pozici�val,
	 * az el�z� transzform�ci�t elmenti
	 * @param g : {@link Graphics2D}
	 * @return tr2 : {@link AffineTransform} - a transzform�lt camera k�p
	 */
	public AffineTransform GetTransform(Graphics2D g) {
		
		 old_transform = g.getTransform();
		 AffineTransform tr2= new AffineTransform(old_transform);
		 tr2.scale(zoom,zoom);
		 tr2.translate(pos_x, pos_y);
		 
		 return tr2;
	}
}
