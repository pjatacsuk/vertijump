package com.vjmp.entities.drawable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.vjmp.entities.Entity.EntityType;
import com.vjmp.entities.drawable.TriggerEntity.TriggerType;
import com.vjmp.gfx.AnimatedSprite;
import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.AnimatedSprite.AnimType;

public class FinishLine extends TriggerEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static AnimatedSprite animatedSprite = null;
	public FinishLine(String string, int x, int y, boolean b) {
		super(string, x, y, b);
		isAlwaysVisible = true;
		
		triggerType = TriggerType.FINISH_LINE;
		
		animatedSprite = new AnimatedSprite("./res/finishflag_anim.png",35,10);
		animatedSprite.setAnimType(AnimType.LOOP);
		animatedSprite.setLocation(this.GetPosX()+sprite.getRectWidth()-animatedSprite.getWidth(),
								   this.GetPosY()+sprite.getRectHeight() + animatedSprite.getHeight());
	}

	public FinishLine(String path, Rectangle rect, boolean b) {
		super(path, rect, b);
		isAlwaysVisible = true;
		triggerType = TriggerType.FINISH_LINE;

		animatedSprite = new AnimatedSprite("./res/finishflag_anim.png",35,10);
		animatedSprite.setAnimType(AnimType.LOOP);
		animatedSprite.setLocation(this.GetPosX()+sprite.getRectWidth()-animatedSprite.getWidth(),
								   this.GetPosY()+sprite.getRectHeight() - animatedSprite.getHeight());
	}
	@Override
	public void update(){
		animatedSprite.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawString("FINISH!!!", GetPosX(), GetPosY() - 100);
		
		animatedSprite.draw(g);
	}
	private void writeObject(ObjectOutputStream stream)
	        throws IOException {
 		
	 		//general Trigger types
	 		stream.writeBoolean(isActive);
	 		stream.writeBoolean(isAlwaysVisible);
	 		stream.writeObject(entityType);
	 		stream.writeObject(triggerType);
	 		
	 		//trigger sprite for editor
	 		stream.writeObject(sprite);
	 		
	 		//finishline specific
	 		stream.writeObject(animatedSprite);
	 		
	}
	private void readObject(ObjectInputStream in)
		 throws IOException, ClassNotFoundException {
			//general Trigger types
			 isActive = in.readBoolean();
			 isAlwaysVisible = in.readBoolean();
			 entityType = (EntityType)in.readObject();
			 triggerType = (TriggerType)in.readObject();
			 
			//trigger sprite for editor
			 sprite = (Sprite)in.readObject();
			 
			 //finish line specific
			 animatedSprite = (AnimatedSprite)in.readObject();
	
	}
}
