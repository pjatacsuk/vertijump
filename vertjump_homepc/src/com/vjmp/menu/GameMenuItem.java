package com.vjmp.menu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

public class GameMenuItem extends AncestorMenuItem {
	boolean isSelected = false;
	boolean isActive = false;
	boolean isHighLightText = false;
	String	name = null;
	Point	position	= null;
	int		width = 0;
	
	public GameMenuItem() {
		isSelected = false;
		name = new String();
		position = new Point();
		menuItemType = MenuItemType.ACTION;
	}
	public boolean isHighLight() {
		return isHighLightText;
	}
	public void setHighLightText(boolean isHighLightText) {
		this.isHighLightText = isHighLightText;
	}
	public GameMenuItem(String string,Point p) {
		isSelected = false;
		name = string;
		position = p;
		menuItemType = MenuItemType.ACTION;
	}
	public GameMenuItem(String string) {
		isSelected = false;
		name = string;
		position = new Point();
		menuItemType = MenuItemType.ACTION;
		
	}
	
	public GameMenuItem(String string, boolean activity) {
		name = string;
		isActive = activity;
		position = new Point();
		menuItemType = MenuItemType.ACTION;
	}
	public void setLocation(Point p){
		position.setLocation(p);
	}
	public void setLocation(int x,int y){
		position.setLocation(x, y);
	}
	public void select(){
		isSelected = true;
	}
	public void deselect(){
		isSelected = false;
	}
	public boolean selected() {
		return isSelected;
	}
	public boolean getActivity() {
		return isActive;
	}
	public void setActivity(boolean isActive){
		this.isActive = isActive;
	}
	public String getName() {
		return name;
	}
	
	void draw(Graphics g) {
		Font font = new Font("Jokerman", Font.PLAIN, 24);
		g.setFont(font);
		if(width == 0) {
			createWidth(g);
		}
		g.drawString(name, position.x,position.y);
	}
	protected void createWidth(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		width = fm.stringWidth(name);
		
		
	}
	public int getWidth() {
		return width;
	}
	public Point getPos() {
		return position;
	}
	public void drawMenuName(Graphics g) {
		
		
	}


	
}
