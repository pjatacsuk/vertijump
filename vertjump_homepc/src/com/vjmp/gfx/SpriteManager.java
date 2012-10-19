package com.vjmp.gfx;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpriteManager implements Iterable<Sprite> {
	private List<Sprite> list;
	
	public SpriteManager() {
		list = new ArrayList<Sprite>();
	}
	
	public void add(Sprite sprite) {
		list.add(sprite);
	}
	public Sprite get(int i) {
		return list.get(i);
		
	}
	public void remove(Sprite sprite) {
		list.remove(sprite);
	}
	public void remove(int i) {
		list.remove(i);
	}
	public Sprite pop(int i) {
		Sprite ret = list.get(i);
		list.remove(i);
		return ret;
	
	}
	
	public void DrawSprites(Graphics g) {
		for(Sprite sprite : list) {
			sprite.draw(g);
		}
	}

	public Sprite getLast() {
		if(list.isEmpty()) return null;
		return list.get(list.size()-1);
	}
	public int size() {
		return list.size();
	}

	@Override
	public Iterator<Sprite> iterator() {
		return list.iterator();
	}
	
}
