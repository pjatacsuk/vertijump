package com.vjmp.gfx;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import com.vjmp.InputHandler;

public class Camera {
	public int pos_x=0;
	public int pos_y=0;
	
	private int width=0;
	private int height=0;
	private int mouse_x;
	private int mouse_y;
	private int editor_speed = 15;
	private AffineTransform old_transform;
	private double zoom=1;
	private boolean zoom_mouse = false;
	public Camera(int width,int height) {
		this.width = width;
		this.height = height;
	/*	pos_x = width / 2;
		pos_y = height / 2; */
	}
	public void update(int player_x,int player_y) {
		//int current_player_x = pos_x - player_x;
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
		if(inputHandler.MOUSE.GetWheelPos() > 0) {
		/*	zoom += 0.05;
			if(zoom > 1) zoom = 1;
			else zoom_mouse = true;
			inputHandler.MOUSE.SetWheelPos(0);
			mouse_x = inputHandler.MOUSE.GetX();
			mouse_y = inputHandler.MOUSE.GetY();
			
			
		//	pos_x -= (inputHandler.MOUSE.GetX()) /( zoom - 0.05) - ((inputHandler.MOUSE.GetX()) / zoom) ;
		//	pos_y -= (inputHandler.MOUSE.GetY()) /(zoom - 0.05) - ((inputHandler.MOUSE.GetY())/ zoom);
		} else if(inputHandler.MOUSE.GetWheelPos() < 0) {
			zoom -= 0.05;
			if(zoom <= 0) zoom = 0.05;
			else zoom_mouse = true;
			mouse_x = inputHandler.MOUSE.GetX();
			mouse_y = inputHandler.MOUSE.GetY();
			inputHandler.MOUSE.SetWheelPos(0);*/
		}
	//	System.out.print((inputHandler.MOUSE.GetX() - pos_x)/zoom +" "+(inputHandler.MOUSE.GetY() - pos_y)/zoom);
	//	System.out.println(" " +((inputHandler.MOUSE.GetX() - pos_x) /( zoom - 0.05) - ((inputHandler.MOUSE.GetX() - pos_x) / zoom)));
		//System.out.println(pos_x +" "+ pos_y);
	}
	public AffineTransform GetOldTransform() {
		return old_transform;
	}
	public AffineTransform GetTransform(Graphics2D g) {
		
	
	/*	 if(zoom_mouse) {
			 Point ptSrc = new Point(mouse_x,mouse_y);
			 Point ptDst_beforezoom = new Point();
			 Point ptDst_afterzoom = new Point();
			 AffineTransform tr = new AffineTransform(old_transform);
			 tr.scale(zoom-0.05, zoom-0.05);
			 tr.transform(ptSrc, ptDst_beforezoom);
			 tr = new  AffineTransform(old_transform);
			 tr.scale(zoom,zoom);
			 tr.transform(ptSrc, ptDst_afterzoom);
			 System.out.println((ptDst_afterzoom.x - ptDst_beforezoom.x) + " " + (ptDst_afterzoom.y - ptDst_beforezoom.y));
		pos_x += -(ptDst_afterzoom.x - ptDst_beforezoom.x);
		pos_y += -(ptDst_afterzoom.y - ptDst_beforezoom.y);
			 zoom_mouse = false;
		 } */
		 old_transform = g.getTransform();
		 AffineTransform tr2= new AffineTransform(old_transform);
		 tr2.scale(zoom,zoom);
		 tr2.translate(pos_x, pos_y);
		 
		 return tr2;
	}
}
