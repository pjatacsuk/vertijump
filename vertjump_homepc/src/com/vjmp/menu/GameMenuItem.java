package com.vjmp.menu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

/**
 * 
 * A játék fõ menüje.
 *
 */
public class GameMenuItem extends AncestorMenuItem {
	boolean isSelected = false;
	boolean isActive = false;
	boolean isHighLightText = false;
	String	name = null;
	Point	position	= null;
	int		width = 0;
	
	/**
	 * Konstruktor
	 */
	public GameMenuItem() {
		isSelected = false;
		name = new String();
		position = new Point();
		menuItemType = MenuItemType.ACTION;
	}
	
	/**
	 * 
	 * @return true - ha highligh-olva van a szöveg, false - ha nincs
	 */
	public boolean isHighLight() {
		return isHighLightText;
	}
	
	/**
	 * Beállitja a isHighLighText változót
	 * @param isHighLightText : boolean - a beállitandó érték
	 */
	public void setHighLightText(boolean isHighLightText) {
		this.isHighLightText = isHighLightText;
	}
	
	/**
	 * Konstruktor
	 * @param string : {@link String} - a menu item neve
	 * @param p : {@link Point} - a menu item poziciója
	 */
	public GameMenuItem(String string,Point p) {
		isSelected = false;
		name = string;
		position = p;
		menuItemType = MenuItemType.ACTION;
	}
	
	/**
	 * Konstruktor
	 * @param string : {@link String} - a menu item neve
	 */
	public GameMenuItem(String string) {
		isSelected = false;
		name = string;
		position = new Point();
		menuItemType = MenuItemType.ACTION;
		
	}
	
	/**
	 * Konstruktor
	 * @param string : {@link String} - a menu item neve
	 * @param activity : boolean -a menu item aktivitása
	 */
	public GameMenuItem(String string, boolean activity) {
		name = string;
		isActive = activity;
		position = new Point();
		menuItemType = MenuItemType.ACTION;
	}
	
	/**
	 * Beállitja a menu itemet az adott pozicióra
	 * @param p : {@link Point} - az adott pozició
	 */
	public void setLocation(Point p){
		position.setLocation(p);
	}
	
	/**
	 * Beállitja a menu itemet az adott pozicióra
	 * @param x : int - az adott x pozició
	 * @param y : int - az adott y pozició
	 */
	public void setLocation(int x,int y){
		position.setLocation(x, y);
	}
	
	/**
	 * select-eli az adott menu itemet
	 */
	public void select(){
		isSelected = true;
	}
	
	/**
	 * deselect-eli az adott menu itemet
	 */
	public void deselect(){
		isSelected = false;
	}
	
	/**
	 * visszatér az adott menu item selected állapotával
	 * @return isSelected : boolean - true - ha selected, false - ha nem
	 */
	public boolean selected() {
		return isSelected;
	}
	
	/**
	 * visszatér az adott menu item aktivitásával
	 * @return isAcitve : boolean - true ha aktiv, false - ha nem
	 */
	public boolean getActivity() {
		return isActive;
	}
	
	/**
	 * Beállitja az adott menu item aktivitását
	 * @param isActive : boolean - az adott menu item aktivitása
	 */
	public void setActivity(boolean isActive){
		this.isActive = isActive;
	}
	
	/**
	 * Visszatér az menu item nevével 
	 * @return ret : {@link String} a menu item neve 
	 * 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Kirajzolja a menu itemet.
	 * @param g : {@link Graphics}
	 */
	void draw(Graphics g) {
		Font font = new Font("Jokerman", Font.PLAIN, 24);
		g.setFont(font);
		if(width == 0) {
			createWidth(g);
		}
		g.drawString(name, position.x,position.y);
	}
	
	/**
	 * Kiszámitja az adott szöveg szélességével.
	 * @param g : {@link Graphics}
	 */
	protected void createWidth(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		width = fm.stringWidth(name);
		
		
	}
	
	/**
	 * Visszatér a szélességgel
	 * @return width : int a - szöveg szélessége
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Visszatér a menu item poziciójával
	 * @return position : {@link Point} - a menu item poziciója
	 */
	public Point getPos() {
		return position;
	}
	
	/**
	 * Kirajzolja a menu nevét
	 * @param g : {@link Graphics}
	 */
	public void drawMenuName(Graphics g) {
		
		
	}


	
}
