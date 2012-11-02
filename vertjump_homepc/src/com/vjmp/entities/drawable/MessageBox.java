package com.vjmp.entities.drawable;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.vjmp.gfx.Sprite;

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
	public MessageBox(String string, int x, int y, boolean b) {
		super(string, x, y, b);
		triggerType = TriggerType.MESSAGE_BOX;
		Rectangle message_box_rect = new Rectangle(x +box_offSet.x,
				   								   y - box_offSet.y,
				   								   200,
				   								   75);
			message_box = new Sprite("./res/message_box.png",message_box_rect,b);
		
	}
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
	public MessageBox(String messageString, String path, Rectangle rect,
			boolean b, boolean[] walls) {
		super(path,rect,b,walls);
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
	@Override
	public void draw(Graphics g) {
		message_box.draw(g);
		if(calculated_strings==null) { 
			calculated_strings = CalculateStringsUpdateRectFromIt(g);
	
		}
	
		for(int i=0;i<calculated_strings.length;i++) {
			g.drawString(calculated_strings[i],message_box.GetPosX()+20,message_box.GetPosY() + 25 + i*string_height+5);
		}
	//	g.drawString(message, message_box.GetPosX()+20,message_box.GetPosY() + 25);
	}
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
