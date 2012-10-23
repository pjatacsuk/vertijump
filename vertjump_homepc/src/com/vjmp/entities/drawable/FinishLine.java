package com.vjmp.entities.drawable;

import java.awt.Rectangle;

public class FinishLine extends DrawableEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FinishLine(String string, int x, int y, boolean b) {
		super(string, x, y, false);
		entityType = EntityType.FINISH_LINE;
	}

	public FinishLine(String path, Rectangle rect, boolean b) {
		super(path,rect,false);
		entityType = EntityType.FINISH_LINE;
		
	}
	
}
