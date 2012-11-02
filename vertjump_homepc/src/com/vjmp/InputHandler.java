package com.vjmp;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.SwingUtilities;
import com.vjmp.editor.MapEditor;


public class InputHandler implements KeyListener,MouseMotionListener,MouseListener,MouseWheelListener {

	public class Key {
		private boolean pressed = false;
		private boolean pressed_and_released = false;
		private boolean previous_state;
		
		Key() {
			
		}
		
		public void setPressed(boolean press) {
			previous_state = pressed;
			pressed = press;
			if(pressed) pressed_and_released = true;
	
			
		}
		public boolean isPressed() {
			return pressed;
		}
		public boolean isPressedAndReleased() {
			if(pressed_and_released) {
				if(!pressed) {
					pressed_and_released = false;
					return true;
				
				}
				else {
					return false;
				}
			} else {
				return false;
			}
		}
		public boolean isPressedOnce() {
			if(pressed == true && previous_state == false){
				pressed = false;
				return true;
			} else {
				return false;
			}
		}
	}
	public enum Buttons {LEFT,RIGHT,MIDDLE,NONE};
	public class Mouse {
		
		private int pos_x = 0;
		private int pos_y = 0;
		
		private int old_pos_x = 0;
		private int old_pos_y = 0;
		private int wheel_pos;
		public Buttons button_clicked = Buttons.NONE;
		public Key button = null;
		
		Mouse() {
			button = new Key();
		}
		
		public int GetX() {
			return pos_x;
		}
		public int GetY() {
			return pos_y;
		}
		public int GetWheelPos() {
			return wheel_pos;
		}
		public void SetX(int x) {
			pos_x = x;
		}
		public void SetY(int y) {
			pos_y = y;
		}
		public void SetWheelPos(int pos) {
			wheel_pos = pos;
		}
		public void SetCoord(int x,int y) {
			old_pos_x = pos_x;
			old_pos_y = pos_y;
			pos_x = x;
			pos_y = y;
		}
		public Point GetOldPos() {
			return new Point(old_pos_x,old_pos_y);
		}
		public Point GetPos() {
			return new Point(pos_x,pos_y);
		}
		public void SetAttributes(int x,int y,int pos) {
			SetCoord(x,y);
			SetWheelPos(pos);
		}

		public void Clicked(int button) {
			if(button == 3) {
				button_clicked = Buttons.RIGHT;
				
			} else if(button ==1) {
				button_clicked = Buttons.LEFT;
			} else if(button == 2) {
				button_clicked = Buttons.MIDDLE;
			}
			this.button.setPressed(true);
		}
		public void setPressed(boolean press) {
			this.button.setPressed(press);
		}
		
	}
	
	public Key W = new Key();
	public Key A = new Key();
	public Key S = new Key();
	public Key D = new Key();
	public Key O = new Key();
	public Key P = new Key();
	public Key SHIFT = new Key();
	public Key ESC = new Key();
	public Key ENTER = new Key();
	
	public Mouse MOUSE = new Mouse();
	
	
	public InputHandler(Game game) {
		game.addKeyListener(this);
	}
	public InputHandler(MapEditor editor) {
		editor.addKeyListener(this);
		editor.addMouseListener(this);
		editor.addMouseWheelListener(this);
		editor.addMouseMotionListener(this);
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
		if(keyCode == KeyEvent.VK_O) {
			O.setPressed(isPressed);
		}
		if(keyCode == KeyEvent.VK_P) {
			P.setPressed(isPressed);
		}
		if(keyCode == KeyEvent.VK_ESCAPE) {
			ESC.setPressed(isPressed);
		}
		if(keyCode == KeyEvent.VK_ENTER){
			ENTER.setPressed(isPressed);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		MOUSE.SetCoord(e.getX(), e.getY());
		MOUSE.Clicked(e.getButton());
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		MOUSE.SetCoord(e.getX(), e.getY());
		MOUSE.button_clicked = Buttons.NONE;
		MOUSE.button.setPressed(false);
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if(arg0.getWheelRotation() > 0) {
			MOUSE.SetAttributes(arg0.getX(),arg0.getX(),-1);
		} else if(arg0.getWheelRotation() < 0){
			MOUSE.SetAttributes(arg0.getX(),arg0.getX(),1);
		}
		
		
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
	//	MOUSE.SetCoord(arg0.getX(),arg0.getY());
	//	System.out.println(arg0.getX()+ " " + arg0.getY());
		
	}
}
