package com.vjmp;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

	class Key {
		private boolean pressed = false;
		
		
		Key() {
			
		}
		
		public void setPressed(boolean press) {
			pressed = press ;
		}
		public boolean isPressed() {
			return pressed;
		}
		
	}
	
	public Key W = new Key();
	public Key A = new Key();
	public Key S = new Key();
	public Key D = new Key();
	public Key SHIFT = new Key();
	
	public InputHandler(Game game) {
		game.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		toogleKey(arg0.getKeyCode(),true);
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		toogleKey(arg0.getKeyCode(),false);
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stuff
		
	}
	private void toogleKey(int keyCode,boolean isPressed) {
		if(keyCode == KeyEvent.VK_W) {
			W.setPressed(isPressed);
		}
		if(keyCode == KeyEvent.VK_A) {
			A.setPressed(isPressed);
		}
		if(keyCode == KeyEvent.VK_S) {
			S.setPressed(isPressed);
		}
		if(keyCode == KeyEvent.VK_D) {
			D.setPressed(isPressed);
		}
		if(keyCode == KeyEvent.VK_SHIFT) {
			SHIFT.setPressed(isPressed);
		}
	}
}
