package com.vjmp.entities.drawable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.vjmp.entities.Entity.EntityType;
import com.vjmp.entities.drawable.TriggerEntity.TriggerType;
import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.Sprite.Dir;
import com.vjmp.gfx.Sprite.SpriteType;

/**
 * A SpikeTrigger olyan {@link TriggerEntity} amellyel való érintkezés a player halálához vezet.
 * A {@link SpikeTrigger}-t egy meglévõ falra kell felhelyezni, a {@link SpikeTrigger} sprite-ja
 * csak editorban látszik
 * @author User
 *
 */
public class SpikeTrigger extends TriggerEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Polygon[] polygons = null;
	private boolean[] spike_walls = null;
	private int	spike_count = 0;
	private static final int spike_width = 15;
	private static final int spike_height = 15;
	
	
	/**
	 * Konstruktor
	 * @param string : {@link String} - a {@link SpikeTrigger} sprite-jának elérési útvonala
	 * @param x : int - a {@link SpikeTrigger} sprite-jának x poziciója
	 * @param y : int - a {@link SpikeTrigger} sprite-jának y poziciója
	 * @param b : boolean - {@link SpikeTrigger} sprite-jának láthatósága
	 */
	public SpikeTrigger(String string, int x, int y, boolean b) {
		super(string, x, y, b);
		// TODO Auto-generated constructor stub
		
		triggerType = TriggerType.SPIKE;
		isAlwaysVisible = true;
	}	
	
	/**
	 * Konstruktor
	 * @param path : {@link String} - a {@link SpikeTrigger} sprite-jának elérési útvonala
	 * @param rect : {@link Rectangle} - a {@link SpikeTrigger} sprite-jának rectangelje
	 * @param b : boolean - {@link SpikeTrigger} sprite-jának láthatósága
	 */
	public SpikeTrigger(String path, Rectangle rect, boolean b) {
		super(path, rect, b);
		
		triggerType = TriggerType.SPIKE;
		isAlwaysVisible = true;
	}
	
	/**
	 * Konstruktor
	 * @param path : {@link String} - a {@link SpikeTrigger} sprite-jának elérési útvonala
	 * @param rect : {@link Rectangle} - a {@link SpikeTrigger} sprite-jának rectangelje
	 * @param b : boolean - {@link SpikeTrigger} sprite-jának láthatósága
	 * @param walls : boolean[] - {@link SpikeTrigger} sprite-jának falai
	 */
	public SpikeTrigger(String path, Rectangle rect, boolean b,boolean[] walls,SpriteType spriteType){
		super(path,rect,b,walls,spriteType);
		isAlwaysVisible = true;
		triggerType = TriggerType.SPIKE;
		this.spike_walls = walls;
		
	}

	/**
	 * Létrehozza a sprite wall változóinak függvényében a tüskéket.
	 * Ha van fal az adott oldalon, akkor tüskék is lesznek.
	 * @param g : {@link Graphics}
	 * @return polygons : Polygon[] - a létrehozott "tüskék" (polygonok) tömbje
	 */
	private Polygon[] CreateSpikes(Graphics g) {
		
		int rect_height = sprite.getRect().height;
		int rect_width  = sprite.getRect().width;
		spike_count = 0;
		
		if(spike_walls[Sprite.getDirIndex(Dir.WEST)]) {
			spike_count += rect_height / spike_height;	
		}
		if(spike_walls[Sprite.getDirIndex(Dir.EAST)]) {
			spike_count += rect_height / spike_height;
			
		}
		if(spike_walls[Sprite.getDirIndex(Dir.SOUTH)]) {
			spike_count += rect_width / spike_width;
		}if(spike_walls[Sprite.getDirIndex(Dir.NORTH)]) {
			spike_count += rect_width / spike_width;
		}
		
		
		
		g.setColor(new Color(188, 79, 99));
		polygons = new Polygon[spike_count];
		if(spike_walls[Sprite.getDirIndex(Dir.WEST)]) {
			CreateWestSpikes(g);
		}
		if(spike_walls[Sprite.getDirIndex(Dir.EAST)]) {
			CreateEastSpikes(g);
		}
		if(spike_walls[Sprite.getDirIndex(Dir.NORTH)]){
			CreateNorthSpikes(g);
		}
		if(spike_walls[Sprite.getDirIndex(Dir.SOUTH)]) {
			CreateSouthSpikes(g);
		}
		return polygons;
	}
	/**
	 * Létrehoz a tüskéket a déli oldalon.
	 * @param g : Graphics
	 */
	private void CreateSouthSpikes(Graphics g) {
		int index = 0;
		while(polygons[index] != null) index++;
		int spike_south_count = sprite.getRect().width / spike_width;	
		for(int i=0;i<spike_south_count;i++) {
			Polygon p = new Polygon();
			p.addPoint(sprite.GetPosX() + i * spike_width ,  sprite.GetPosY() + sprite.getRectHeight());
			p.addPoint(sprite.GetPosX() + (i+1) * spike_width , sprite.GetPosY() + sprite.getRectHeight());
			p.addPoint(sprite.GetPosX() + i * spike_width + spike_width/2, sprite.GetPosY() + spike_height + sprite.getRectHeight());
			g.fillPolygon(p);
			polygons[index] = p;
			index++;
		};
		
		
	}

	/**
	 * Létrehozza a tüskéket az északi oldalon
	 * @param g : {@link Graphics}
	 */
	private void CreateNorthSpikes(Graphics g) {
		int index = 0;
		while(polygons[index] != null) index++;
		int spike_north_count = ( sprite.getRect().width / spike_width);	
		for(int i=0;i<spike_north_count;i++) {
			Polygon p = new Polygon();
			p.addPoint(sprite.GetPosX() + i * spike_width ,  sprite.GetPosY());
			p.addPoint(sprite.GetPosX() + (i+1) * spike_width , sprite.GetPosY());
			p.addPoint(sprite.GetPosX() + i * spike_width + spike_width/2, sprite.GetPosY() - spike_height);
			g.fillPolygon(p);
			polygons[index] = p;
			index++;

		};
		
	}

	/**
	 * Létrehozza a tüskéket a keleti oldalon
	 * @param g : {@link Graphics}
	 */
	private void CreateEastSpikes( Graphics g) {
		int index = 0;
		while(polygons[index] != null) index++;
		int spike_east_count = (sprite.getRect().height / spike_height);	
		for(int i=0;i<spike_east_count;i++) {
			Polygon p = new Polygon();
			p.addPoint(sprite.GetPosX() + sprite.getRectWidth(),  sprite.GetPosY() + i * spike_height);
			p.addPoint(sprite.GetPosX() + sprite.getRectWidth(), sprite.GetPosY() + (i+1)*spike_width);
			p.addPoint(sprite.GetPosX() + sprite.getRectWidth() + spike_width, sprite.GetPosY() + i * spike_height + spike_height/2);
			g.fillPolygon(p);
			polygons[index] = p;
			index++;
		};
		
		
	}

	/**
	 * Létrehozza a tüskéket a nyugati oldalon
	 * @param g : {@link Graphics}
	 * @return polygons : Polygon[] - a spike-okat tartalmazó {@link Polygon}n tömb
	 */
	public Polygon[] CreateWestSpikes(Graphics g) {
		int index = 0;
		while(polygons[index] != null) index++;
		int spike_west_count = (sprite.getRect().height / spike_height);	
		for(int i=0;i<spike_west_count;i++) {
			Polygon p = new Polygon();
			p.addPoint(sprite.GetPosX(),  sprite.GetPosY() + i * spike_height);
			p.addPoint(sprite.GetPosX(), sprite.GetPosY() + (i+1)*spike_width);
			p.addPoint(sprite.GetPosX() - spike_width, sprite.GetPosY() + i * spike_height + spike_height/2);
			g.fillPolygon(p);
			polygons[index] = p;
			index++;

		};
		return polygons;
	}
	
	/**
	 * Kirajzolja a tüskéket
	 * @param g : {@link Graphics}
	 */
	private void drawSpikes(Graphics g) {
		if(polygons == null) {
			polygons = CreateSpikes(g);
			
		}
		g.setColor(new Color(188, 79, 99));
		for(int i=0;i<spike_count;i++) {
			g.fillPolygon(polygons[i]);
			g.drawPolygon(polygons[i]);
			
		}
		g.setColor(Color.black); 
	}
	
	/**
	 * Kirajzolja a kirajzolandót.
	 */
	@Override
	public void draw(Graphics g) {
			drawSpikes(g);
				
	}
	
	/**
	 * "Disaktiválja" a {@link TriggerEntity}-t
	 */
	@Override 
	public void disactivateTrigger() {
		return;
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
	 		
	 		//spike
	 		for(int i=0;i<4;i++) {
	 			stream.writeBoolean(spike_walls[i]);
	 		}
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
		 
		 //spike
		 spike_walls = new boolean[4];
		 for(int i=0;i<4;i++) {
			 spike_walls[i] = in.readBoolean();
		 }
		
	}
	

}
