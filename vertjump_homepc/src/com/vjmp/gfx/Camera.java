package com.vjmp.gfx;

public class Camera {
	public int pos_x=0;
	public int pos_y=0;
	
	private int width=0;
	private int height=0;
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
}
