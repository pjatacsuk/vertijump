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
 * A class felelõs a beérkezõ inputok kezelésére, mind a mouse, mind a key inputokat beleértve.
 * Implementálja a {@link KeyListener}, {@link MouseMotionListener}, {@link MouseListener}, {@link MouseWheelListener}
 * interface-eket.
 */
public class InputHandler implements KeyListener,MouseMotionListener,MouseListener,MouseWheelListener {

	/**
	 * Belsõ class, amely a billentyûgombok kezelését végzi
	 *
	 */
	public class Key {
		private boolean pressed = false;
		private boolean pressed_and_released = false;
		private boolean previous_state;
		
		Key() {
			
		}
		
		/**
		 * A lenyomott/felengedett gomb állapotát kezeli
		 * @param press : boolean 
		 */
		public void setPressed(boolean press) {
			previous_state = pressed;
			pressed = press;
			//ha a felengedést vizsgáljuk
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
		 * Megadja, hogy a gomb fel lett-e már engedve a lenyomása után
		 * @return true - ha a fel lett engedve a lenyomás után<br>
		 *         false - ha nem lett lenyomva, vagy lenyomás után nem lettfelengedve
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
		 * Biztositja, hogy a lenyomott gombot csak egyszer érzékeljük, pergésmentesit
		 * @return true - ha az elõzõ status-a gombnak false és a jelenlegi true <br>
		 * 		   false - ha nem lett lenyomva, vagy pedig az elõzõ állapotban is levolt már nyomva
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
	 * Az egér gombok enumja
	 *
	 */
	public enum Buttons {LEFT,RIGHT,MIDDLE,NONE};
	
	/**
	 * 
	 * Az egér kezelését végzõ osztály, mind a mozgást mind a lenyomott egérgombot kezeli
	 * Egyszerre csak egy egérgomb lenyomását kezeljük 
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
		 * @return int - A mouse X poziciója
		 */
		public int GetX() {
			return pos.x;
		}
		
		/**
		 * @return int - A mouse Y poziciója
		 */
		public int GetY() {
			return pos.y;
		}
		
		/**
		 * 
		 * @return int - A mouse wheel poziciója
		 */
		public int GetWheelPos() {
			return wheel_pos;
		}
		
		/**
		 * 
		 * @param x : int - A mouse kivánt X poziciója
		 */
		public void SetX(int x) {
			pos.x = x;
		}
		
		/**
		 * 
		 * @param y : int - A mouse kivánt Y poziciója
		 */
		public void SetY(int y) {
			pos.y = y;
		}
		
		/**
		 * 
		 * @param pos : int - A mousewheel kivánt poziciója
		 */
		public void SetWheelPos(int pos) {
			wheel_pos = pos;
		}
		
		/**
		 * 
		 * @param point : Point - A mouse kivánt poziciója
		 */
		public void SetCoord(Point point) {
			old_pos = pos;
			pos = point;
		}
		/**
		 * 
		 * @return old_pos : Point - A mouse elõzõ poziciója
		 */
		public Point getOldPos() {
			return old_pos; 
		}
		
		/**
		 * 
		 * @return pos : Point - A mouse jelenlegi poziciója
		 */
		public Point getPos() {
			return pos; 
		}
		
		/**
		 * 
		 * @return dragged_pos : Point - A mouse "dragged" poziciója
		 */
		public Point getDraggedPos(){
			return dragged_pos;
		}
		
		/**
		 * 
		 * @param point : Point - A mouse kivánt poziciója
		 * @param pos : int - A mousewheel kivánt poziciója
		 */
		public void SetAttributes(Point point,int pos) {
			SetCoord(point);
			SetWheelPos(pos);
		}

		/**
		 * Elvégzi a mousegomb lenyomást
		 * @param button : int - A lenyomott mousegomb kódja
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
	 * Konstruktor a játékhoz
	 * @param game - {@link Game} - A játék fõclassja
	 */
	public InputHandler(Game game) {
		game.addKeyListener(this);
	}
	
	/**
	 * Konstruktor az editorhoz
	 * @param editor : {@link MapEditor} - A játék mapeditorja
	 */
	public InputHandler(MapEditor editor) {
		editor.addKeyListener(this);
		editor.addMouseListener(this);
		editor.addMouseWheelListener(this);
		editor.addMouseMotionListener(this);
	}

	/**
	 * implementált függvény, elvégzi a gomb lenyomást
	 * @param arg0 : KeyEvent
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		if(!arg0.isAltDown()) {
			toogleKey(arg0.getKeyCode(),true);
		}
	}

	/**
	 * implementált függvény, elvégzi a gomb felengedést
	 * @param arg0
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		if(!arg0.isAltDown()){
			toogleKey(arg0.getKeyCode(),false);
		}
	}

	/** 
	 * void függvény - nincs lényeges implementálása
	 * @param arg0 : KeyEvent
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stuff
		
	}
	/**
	 * A saját gomblenyomást kezelõ függvény
	 * @param keyCode : int - a gomb kódja
	 * @param isPressed : boolean - a gomb állapota
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
	 * Mouse gomb lenyomását kezeli
	 * @param e : {@link MouseEvent}
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		MOUSE.SetCoord(e.getPoint());
		MOUSE.Clicked(e.getButton());
		
	}
	
	/**
	 * A Mouse gomb felengedését kezeli
	 * @param e : {@link MouseEvent}
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		MOUSE.SetCoord(e.getPoint());
		MOUSE.button_clicked = Buttons.NONE;
		MOUSE.button.setPressed(false);
	}
	
	/**
	 * A mouse wheel mozgását kezeli
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
	 * A mouse draggelését kezlei
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
