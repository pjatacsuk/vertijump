package com.vjmp;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import com.vjmp.editor.MapEditor;

/**
 * 
 * A class felel�s a be�rkez� inputok kezel�s�re, mind a mouse, mind a key inputokat bele�rtve.
 * Implement�lja a {@link KeyListener}, {@link MouseMotionListener}, {@link MouseListener}, {@link MouseWheelListener}
 * interface-eket.
 */
public class InputHandler implements KeyListener,MouseMotionListener,MouseListener,MouseWheelListener {

	/**
	 * Bels� class, amely a billenty�gombok kezel�s�t v�gzi
	 *
	 */
	public class Key {
		private boolean pressed = false;
		private boolean pressed_and_released = false;
		private boolean previous_state;
		
		Key() {
			
		}
		
		/**
		 * A lenyomott/felengedett gomb �llapot�t kezeli
		 * @param press : boolean 
		 */
		public void setPressed(boolean press) {
			previous_state = pressed;
			pressed = press;
			//ha a felenged�st vizsg�ljuk
			if(pressed) pressed_and_released = true;
	
			
		}
		
		/**
		 * Megadja, hogy le van-e nyomva a gomb. 
		 * @return true - ha le van nyomva a gomb
		 *         <br>false - ha nincs lenyomva a gomb
		 */
		public boolean isPressed() {
			return pressed;
		}
		
		/**
		 * Megadja, hogy a gomb fel lett-e m�r engedve a lenyom�sa ut�n
		 * @return true - ha a fel lett engedve a lenyom�s ut�n<br>
		 *         false - ha nem lett lenyomva, vagy lenyom�s ut�n nem lettfelengedve
		 */
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
		
		/**
		 * Biztositja, hogy a lenyomott gombot csak egyszer �rz�kelj�k, perg�smentesit
		 * @return true - ha az el�z� status-a gombnak false �s a jelenlegi true <br>
		 * 		   false - ha nem lett lenyomva, vagy pedig az el�z� �llapotban is levolt m�r nyomva
		 * */
		public boolean isPressedOnce() {
			if(pressed == true && previous_state == false){
				pressed = false;
				return true;
			} else {
				return false;
			}
		}
	}
	/**
	 * 
	 * Az eg�r gombok enumja
	 *
	 */
	public enum Buttons {LEFT,RIGHT,MIDDLE,NONE};
	
	/**
	 * 
	 * Az eg�r kezel�s�t v�gz� oszt�ly, mind a mozg�st mind a lenyomott eg�rgombot kezeli
	 * Egyszerre csak egy eg�rgomb lenyom�s�t kezelj�k 
	 */
	public class Mouse {
		
		private Point pos  = null;
		private Point old_pos = null;
		private Point dragged_pos = null;
		
		private int wheel_pos;
		public Buttons button_clicked = Buttons.NONE;
		public Key button = null;
		/**
		 * Konstruktor
		 */
		Mouse() {
			button = new Key();
		}
		
		/**
		 * @return int - A mouse X pozici�ja
		 */
		public int GetX() {
			return pos.x;
		}
		
		/**
		 * @return int - A mouse Y pozici�ja
		 */
		public int GetY() {
			return pos.y;
		}
		
		/**
		 * 
		 * @return int - A mouse wheel pozici�ja
		 */
		public int GetWheelPos() {
			return wheel_pos;
		}
		
		/**
		 * 
		 * @param x : int - A mouse kiv�nt X pozici�ja
		 */
		public void SetX(int x) {
			pos.x = x;
		}
		
		/**
		 * 
		 * @param y : int - A mouse kiv�nt Y pozici�ja
		 */
		public void SetY(int y) {
			pos.y = y;
		}
		
		/**
		 * 
		 * @param pos : int - A mousewheel kiv�nt pozici�ja
		 */
		public void SetWheelPos(int pos) {
			wheel_pos = pos;
		}
		
		/**
		 * 
		 * @param point : Point - A mouse kiv�nt pozici�ja
		 */
		public void SetCoord(Point point) {
			old_pos = pos;
			pos = point;
		}
		/**
		 * 
		 * @return old_pos : Point - A mouse el�z� pozici�ja
		 */
		public Point getOldPos() {
			return old_pos; 
		}
		
		/**
		 * 
		 * @return pos : Point - A mouse jelenlegi pozici�ja
		 */
		public Point getPos() {
			return pos; 
		}
		
		/**
		 * 
		 * @return dragged_pos : Point - A mouse "dragged" pozici�ja
		 */
		public Point getDraggedPos(){
			return dragged_pos;
		}
		
		/**
		 * 
		 * @param point : Point - A mouse kiv�nt pozici�ja
		 * @param pos : int - A mousewheel kiv�nt pozici�ja
		 */
		public void SetAttributes(Point point,int pos) {
			SetCoord(point);
			SetWheelPos(pos);
		}

		/**
		 * Elv�gzi a mousegomb lenyom�st
		 * @param button : int - A lenyomott mousegomb k�dja
		 */
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

		public void setDraggedPos(Point p) {
			dragged_pos = p;
			
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
	public Key CTRL = new Key();
	
	public Mouse MOUSE = new Mouse();
	
	/**
	 * Konstruktor a j�t�khoz
	 * @param game - {@link Game} - A j�t�k f�classja
	 */
	public InputHandler(Game game) {
		game.addKeyListener(this);
	}
	
	/**
	 * Konstruktor az editorhoz
	 * @param editor : {@link MapEditor} - A j�t�k mapeditorja
	 */
	public InputHandler(MapEditor editor) {
		editor.addKeyListener(this);
		editor.addMouseListener(this);
		editor.addMouseWheelListener(this);
		editor.addMouseMotionListener(this);
	}

	/**
	 * implement�lt f�ggv�ny, elv�gzi a gomb lenyom�st
	 * @param arg0 : KeyEvent
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(!arg0.isAltDown()) {
			toogleKey(arg0.getKeyCode(),true);
		}
	}

	/**
	 * implement�lt f�ggv�ny, elv�gzi a gomb felenged�st
	 * @param arg0
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		if(!arg0.isAltDown()){
			toogleKey(arg0.getKeyCode(),false);
		}
	}

	/** 
	 * void f�ggv�ny - nincs l�nyeges implement�l�sa
	 * @param arg0 : KeyEvent
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stuff
		
	}
	/**
	 * A saj�t gomblenyom�st kezel� f�ggv�ny
	 * @param keyCode : int - a gomb k�dja
	 * @param isPressed : boolean - a gomb �llapota
	 */
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
		if(keyCode == KeyEvent.VK_CONTROL){
			CTRL.setPressed(isPressed);
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
	
	/**
	 * Mouse gomb lenyom�s�t kezeli
	 * @param e : {@link MouseEvent}
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		MOUSE.SetCoord(e.getPoint());
		MOUSE.Clicked(e.getButton());
		
	}
	
	/**
	 * A Mouse gomb felenged�s�t kezeli
	 * @param e : {@link MouseEvent}
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		MOUSE.SetCoord(e.getPoint());
		MOUSE.button_clicked = Buttons.NONE;
		MOUSE.button.setPressed(false);
	}
	
	/**
	 * A mouse wheel mozg�s�t kezeli
	 * @param arg0 : {@link MouseWheelEvent}
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if(arg0.getWheelRotation() > 0) {
			MOUSE.SetAttributes(arg0.getPoint(),-1);
		} else if(arg0.getWheelRotation() < 0){
			MOUSE.SetAttributes(arg0.getPoint(),1);
		}
		
		
	}
	
	/**
	 * A mouse draggel�s�t kezlei
	 * @param arg0 : {@link MouseEvent}
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {
		MOUSE.dragged_pos = arg0.getPoint();
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}	
	
}
