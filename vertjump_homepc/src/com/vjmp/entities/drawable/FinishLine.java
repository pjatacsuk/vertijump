package com.vjmp.entities.drawable;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.vjmp.Player;
import com.vjmp.entities.Entity.EntityType;
import com.vjmp.entities.drawable.TriggerEntity.TriggerType;
import com.vjmp.gfx.AnimatedSprite;
import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.AnimatedSprite.AnimType;

/**
 * A {@link FinishLine}Line jelzi a pálya végét. A pálya teljesitéséhez a {@link FinishLine} által
 * megadott {@link Rectangle}-el kell ütköznie a {@link Player} -nek
 * 
 *
 */
public class FinishLine extends TriggerEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  AnimatedSprite animatedSprite = null;
	
	/**
	 * Konstruktor
	 * @param string : {@link String} - a sprite elérési útvonala
	 * @param x : int - a {@link FinishLine} x poziciója
	 * @param y : int - a {@link FinishLine} y poziciója
	 * @param b : boolean - a {@link Sprite} láthatósága
	 */
	public FinishLine(String string, int x, int y, boolean b) {
		super(string, x, y, b);
		isAlwaysVisible = true;
		
		triggerType = TriggerType.FINISH_LINE;
		
		//a flag animáció elhelyezése a rectangle végében
		animatedSprite = new AnimatedSprite("./res/finishflag_anim.png",35,10);
		animatedSprite.setAnimType(AnimType.LOOP);
		animatedSprite.setLocation(this.GetPosX()+sprite.getRectWidth()-animatedSprite.getWidth(),
								   this.GetPosY()+sprite.getRectHeight() + animatedSprite.getHeight());
	}

	/**
	 * Konstruktor
	 * @param path : {@link String} - a {@link FinishLine} sprite elérési útvonala
	 * @param rect : {@link Rectangle} - a {@link FinishLine} sprite rectanglje
	 * @param b : boolean - a {@link FinishLine} sprite láthatósága
	 * */
	public FinishLine(String path, Rectangle rect, boolean b) {
		super(path, rect, b);
		isAlwaysVisible = true;
		triggerType = TriggerType.FINISH_LINE;

		//a flag animáció elhelyezése a rectangle végében
		animatedSprite = new AnimatedSprite("./res/finishflag_anim.png",35,10);
		animatedSprite.setAnimType(AnimType.LOOP);
		animatedSprite.setLocation(this.GetPosX()+sprite.getRectWidth()-animatedSprite.getWidth(),
								   this.GetPosY()+sprite.getRectHeight() - animatedSprite.getHeight());
	}
	
	/**
	 * Elvégzi az update-eléseket
	 */
	@Override
	public void update(){
		animatedSprite.update();
	}

	/**
	 * Kirajzolja a kirajzolandót.
	 * @param g : {@link Graphics}
	 */
	@Override
	public void draw(Graphics g) {
		//csak a flag-et kell kirajzolni
		animatedSprite.draw(g);
	}
	
	/**
	 * Serializáció
	 * @param stream : {@link ObjectOutputStream}
	 * @throws IOException
	 */
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
	
	/**
	 * Deserializáció
	 * @param in : {@link ObjectInputStream}
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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
