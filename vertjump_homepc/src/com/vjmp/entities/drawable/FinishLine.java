package com.vjmp.entities.drawable;

import java.awt.Graphics;
import java.awt.Rectangle;

public class FinishLine extends TriggerEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FinishLine(String string, int x, int y, boolean b) {
		super(string, x, y, b);
		triggerType = TriggerType.FINISH_LINE;
	}

	public FinishLine(String path, Rectangle rect, boolean b) {
		super(path, rect, b);
		triggerType = TriggerType.FINISH_LINE;

	}

	@Override
	public void draw(Graphics g) {
		g.drawString("FINISH!!!", GetPosX(), GetPosY() - 100);
	}
}
