package com.vjmp.entities.drawable;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.Sprite.SpriteType;


/**
 * A {@link MessageBox} végzi a pályán a pálya szerkesztõben elhelyezett üzenetek kezelését, megjelenitését
 * frissitését.
 *
 */
public class MessageBox extends TriggerEntity {

	
	private String message;
	private Sprite message_box;
	private String[] calculated_strings = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private static Point box_offSet = new Point(75,75);
    private int string_height = 0;
    
    
    /**
     * Konstruktor
     * @param string : {@link String} - a {@link MessageBox} sprite elérési útvonala
     * @param x : int - a {@link MessageBox} sprite x poziciója
     * @param y : int - a {@link MessageBox} sprite y poziciója
     * @param b : boolean - a  {@link MessageBox} sprite láthatósága
     */
	public MessageBox(String string, int x, int y, boolean b) {
		super(string, x, y, b);
		triggerType = TriggerType.MESSAGE_BOX;
		Rectangle message_box_rect = new Rectangle(x +box_offSet.x,
				   								   y - box_offSet.y,
				   								   200,
				   								   75);
			message_box = new Sprite("./res/message_box.png",message_box_rect,b);
		
	}
	  /**
     * Konstruktor
     * @param path : {@link String} - a {@link MessageBox} sprite elérési útvonala
     * @param rect : {@link Rectangle} - a  {@link MessageBox} sprite {@link Rectangle}-je
     * @param b : boolean - a {@link MessageBox} sprite láthatósága
     */
	public MessageBox(String message,String path, Rectangle rect, boolean b) {
		super(path,rect,b);
		this.message = message;
		triggerType = TriggerType.MESSAGE_BOX;

		int top_right = rect.x + box_offSet.x + 250; 
		
		while(top_right > 3*180) top_right--;
		
		Rectangle message_box_rect = new Rectangle(top_right - 250,
												   rect.y - box_offSet.y*2,
													250,
													75);
		message_box = new Sprite("./res/message_box.png",message_box_rect,b);
	}
	
	/**
	 * Konstruktor
	 * @param messageString : {@link String} - a megadott üzenet
	 * @param path : {@link String} - a {@link MessageBox} sprite elérési útvonala
	 * @param rect : {@link Rectangle} - a {@link MessageBox} sprite {@link Rectangle}-je
	 * @param b : boolean - a {@link MessageBox} sprite láthatósága
	 * @param walls : boolean[] - a {@link MessageBox} sprite falai
	 * @param spriteType : {@link MessageBox} sprite tipusa
	 */
	public MessageBox(String messageString, String path, Rectangle rect,
			boolean b, boolean[] walls,SpriteType spriteType) {
		super(path,rect,b,walls,spriteType);
		this.message = messageString;
		triggerType = TriggerType.MESSAGE_BOX;

		int top_right = rect.x + box_offSet.x + 250; 
		
		while(top_right > 3*180) top_right--;
		
		Rectangle message_box_rect = new Rectangle(top_right - 250,
												   rect.y - box_offSet.y*2,
													250,
													75);
		message_box = new Sprite("./res/message_box.png",message_box_rect,b);
		
	}
	
	/**
	 * Kikalkulálja a messageString által megadott üzenet tördelését, ez alapján
	 * pedig létrehozza a megfelelõ nagyságú rectangle-t a megjelenitéshez
	 * @param g : {@link Graphics}
	 * @return ret : {@link String} tömb - a tördelt messageString
	 */
	public String[] CalculateStringsUpdateRectFromIt(Graphics g) {
		FontMetrics metrics = g.getFontMetrics();
		
		double string_width = metrics.stringWidth(message);
		string_height = metrics.getHeight();
		
		int char_width = metrics.stringWidth("a");
		
		int rows = (int) (string_width / (message_box.getRect().getWidth() - 40)) + 1; 
		String[] ret = new String[rows];
		
		if(rows*(string_height+5)+40 > message_box.getRect().height) {
			message_box.getRect().height = rows * (string_height+5) + 40;
		}
		
		int char_per_row = (int)((message_box.getRect().getWidth()- 40)/char_width);
		
		for(int i=0;i<rows;i++){
			if(message.length() - (i+1)*char_per_row < 0) {
				ret[i] = message.substring(i*char_per_row);
			} else {
				ret[i] = new String(message.substring(i*char_per_row,(i+1)*char_per_row));
			}
		}
		return ret;
	}
	/**
	 * Kiirja a megadott tördelt message-t,
	 * majd kirajzolja a sprite-ot
	 */
	@Override
	public void draw(Graphics g) {
		message_box.draw(g);
		if(calculated_strings==null) { 
			calculated_strings = CalculateStringsUpdateRectFromIt(g);
	
		}
	
		for(int i=0;i<calculated_strings.length;i++) {
			g.drawString(calculated_strings[i],message_box.GetPosX()+20,message_box.GetPosY() + 25 + i*string_height+5);
		}
	}
	/**
	 * Serializáció
	 * @param stream : {@link ObjectOutputStream}
	 * @throws IOException
	 */
	 private void writeObject(ObjectOutputStream stream)
		        throws IOException {
		 		//triggerentity altalanos
		 		stream.writeBoolean(isActive);
		 		stream.writeObject(entityType);
		 		stream.writeObject(triggerType);
		 		stream.writeObject(sprite);
		 		
		 		//messagebox specifikus
		 		stream.writeUTF(message);
		 		stream.writeObject(message_box);
		 		
	}
	 /**
	  *Deserializáció 
	  * @param in : {@link ObjectInputStream}
	  * @throws IOException
	  * @throws ClassNotFoundException
	  */
	 private void readObject(ObjectInputStream in)
			 throws IOException, ClassNotFoundException {
		 //triggerentity altalanos
		 isActive = in.readBoolean();
		 entityType = (EntityType)in.readObject();
		 triggerType = (TriggerType)in.readObject();
		 sprite = (Sprite)in.readObject();
		 
		 //messagebox specifikus
		 message = in.readUTF();
		 message_box  = (Sprite)in.readObject();
		 
		
	}

	
}
