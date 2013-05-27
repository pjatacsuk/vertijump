package com.vjmp;

import java.awt.Rectangle;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.vjmp.entities.drawable.DrawableEntity;
import com.vjmp.managers.DrawableEntityManager;

public class DrawableEntityManagerTest {
		DrawableEntityManager dem = null;
		
		@Before
		public void setUp() {
			dem = new DrawableEntityManager();
		}
		@Test
		public void testAdd() {
			dem.add(new DrawableEntity("./res/png/spooky.png",new Rectangle(0,0,50,50),true));
			dem.add(new DrawableEntity("./res/png/spooky.png",new Rectangle(0,0,50,50),true));
			
			Assert.assertEquals(2,dem.size());
			
			dem.add(new DrawableEntity("./res/png/spooky.png",new Rectangle(0,0,50,50),true));
			Assert.assertEquals(3,dem.size());
		}

		@Test
		public void testRemove(){
			dem.add(new DrawableEntity("./res/png/spooky.png",new Rectangle(0,0,50,50),true));
			dem.add(new DrawableEntity("./res/png/spooky.png",new Rectangle(0,0,50,50),true));
			
			Assert.assertEquals(2, dem.size());
			dem.remove(0);
			
			Assert.assertEquals(1, dem.size());
		}
		
		@Test
		public void testRemoveRect(){
			dem.add(new DrawableEntity("./res/png/spooky.png",new Rectangle(0,0,50,50),true));
			dem.add(new DrawableEntity("./res/png/spooky.png",new Rectangle(1,1,50,50),true));
			
			Assert.assertEquals(2, dem.size());
			
			dem.remove(new Rectangle(-5,-5,55,55));
			
			Assert.assertEquals(0, dem.size());
		}
}

