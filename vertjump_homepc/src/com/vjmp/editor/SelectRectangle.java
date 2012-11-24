package com.vjmp.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.vjmp.InputHandler;
import com.vjmp.gfx.Camera;

public class SelectRectangle {
	private Editor editor = null;
	private Point		 select_start_pos = null;
	private Rectangle	 rect = null;
	public SelectRectangle(Editor editor){
		this.editor = editor;
	}
	
	public void update(Camera camera){
		updateRectangle(camera);
	}
	

	private void updateRectangle(Camera camera) {
		if(editor.getInputHandler().MOUSE.button.isPressed()) {
			if(select_start_pos == null) {
				select_start_pos = editor.getInputHandler().MOUSE.getPos();
				select_start_pos = editor.AddCameraPos(editor.DivBlockSize(select_start_pos),camera);
			}
			Point current_pos = editor.getInputHandler().MOUSE.getDraggedPos();
			if(current_pos != null) {
				current_pos = editor.AddCameraPos(editor.DivBlockSize(current_pos), camera);
				int width = Math.abs(select_start_pos.x - current_pos.x) + editor.getBlockSize();
				int height = Math.abs(select_start_pos.y - current_pos.y) + editor.getBlockSize();
		
				int x = Math.min(current_pos.x,select_start_pos.x);
				int y = Math.min(current_pos.y, select_start_pos.y);
			
				//le null-ozzuk a dragged_pos-t, hogy ne a jelenlegi értéket használja fel késõbbi selectnél
				editor.getInputHandler().MOUSE.setDraggedPos(null);
				
				rect = new Rectangle(x,y,width,height);
				
			}
		} else {
			select_start_pos = null;
			rect = null;
		}
	}

	

	public void draw(Graphics g){
		Color tmp = g.getColor();
		g.setColor(new Color(20,20,20,20));
		if(rect != null) {
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		}
		g.setColor(tmp);
	}
}
