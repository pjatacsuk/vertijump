package com.vjmp.gfx;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import com.vjmp.InputHandler;

public class Camera {
	public int pos_x=0;
	public int pos_y=0;
	
	public int width=0;
	public int height=0;
	private int mouse_x;
	private int mouse_y;
	private int editor_speed = 15;
	private AffineTransform old_transform;
	private double zoom=1;
	private boolean zoom_mouse = false;
	public Camera(int width,int height) {
		this.width = width;
		this.height = height;
	}
	public void update(int player_x,int player_y) {
		
		int current_player_y = player_y + pos_y;
		if(current_player_y < (double)(height / 3)) {
			pos_y += height/3 - current_player_y;
		}
	}
	
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
	public AffineTransform GetOldTransform() {
		return old_transform;
	}
	public AffineTransform GetTransform(Graphics2D g) {
		
		 old_transform = g.getTransform();
		 AffineTransform tr2= new AffineTransform(old_transform);
		 tr2.scale(zoom,zoom);
		 tr2.translate(pos_x, pos_y);
		 
		 return tr2;
	}
}
