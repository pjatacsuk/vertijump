package com.vjmp.menu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

/**
 * 
 * A j�t�k f� men�je.
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
	 * @return true - ha highligh-olva van a sz�veg, false - ha nincs
	 */
	public boolean isHighLight() {
		return isHighLightText;
	}
	
	/**
	 * Be�llitja a isHighLighText v�ltoz�t
	 * @param isHighLightText : boolean - a be�llitand� �rt�k
	 */
	public void setHighLightText(boolean isHighLightText) {
		this.isHighLightText = isHighLightText;
	}
	
	/**
	 * Konstruktor
	 * @param string : {@link String} - a menu item neve
	 * @param p : {@link Point} - a menu item pozici�ja
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
	 * @param activity : boolean -a menu item aktivit�sa
	 */
	public GameMenuItem(String string, boolean activity) {
		name = string;
		isActive = activity;
		position = new Point();
		menuItemType = MenuItemType.ACTION;
	}
	
	/**
	 * Be�llitja a menu itemet az adott pozici�ra
	 * @param p : {@link Point} - az adott pozici�
	 */
	public void setLocation(Point p){
		position.setLocation(p);
	}
	
	/**
	 * Be�llitja a menu itemet az adott pozici�ra
	 * @param x : int - az adott x pozici�
	 * @param y : int - az adott y pozici�
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
	 * visszat�r az adott menu item selected �llapot�val
	 * @return isSelected : boolean - true - ha selected, false - ha nem
	 */
	public boolean selected() {
		return isSelected;
	}
	
	/**
	 * visszat�r az adott menu item aktivit�s�val
	 * @return isAcitve : boolean - true ha aktiv, false - ha nem
	 */
	public boolean getActivity() {
		return isActive;
	}
	
	/**
	 * Be�llitja az adott menu item aktivit�s�t
	 * @param isActive : boolean - az adott menu item aktivit�sa
	 */
	public void setActivity(boolean isActive){
		this.isActive = isActive;
	}
	
	/**
	 * Visszat�r az menu item nev�vel 
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
	 * Kisz�mitja az adott sz�veg sz�less�g�vel.
	 * @param g : {@link Graphics}
	 */
	protected void createWidth(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		width = fm.stringWidth(name);
		
		
	}
	
	/**
	 * Visszat�r a sz�less�ggel
	 * @return width : int a - sz�veg sz�less�ge
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Visszat�r a menu item pozici�j�val
	 * @return position : {@link Point} - a menu item pozici�ja
	 */
	public Point getPos() {
		return position;
	}
	
	/**
	 * Kirajzolja a menu nev�t
	 * @param g : {@link Graphics}
	 */
	public void drawMenuName(Graphics g) {
		
		
	}


	
}
