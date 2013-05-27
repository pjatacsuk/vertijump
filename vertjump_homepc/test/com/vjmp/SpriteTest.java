package com.vjmp;

import java.awt.Point;
import java.awt.Rectangle;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.vjmp.gfx.Sprite;
import com.vjmp.gfx.Sprite.Dir;
import com.vjmp.gfx.Sprite.SpriteType;

public class SpriteTest {
		Sprite sprite;
		
		@Before
		public void setUp(){
			boolean[] wall = {false,false,true,true};
			Rectangle rect = new Rectangle(0,0,100,100);
			sprite = new Sprite("./res/png/spooky.png",rect,true,wall,SpriteType.NORMAL);
			
		}
		
		@Test
		public void testMove() {
			sprite.move(50,50);
			Point pos = sprite.GetLocation();
			Assert.assertEquals(new Point(50,50),pos);
		}
		
		@Test
		public void testWall(){
			Assert.assertEquals(false,sprite.GetWall(Dir.NORTH));
			Assert.assertEquals(true, sprite.GetWall(Dir.SOUTH));
			Assert.assertEquals(true, sprite.GetWall(Dir.EAST));
			Assert.assertEquals(false, sprite.GetWall(Dir.WEST));
			
		}
		
		
}
