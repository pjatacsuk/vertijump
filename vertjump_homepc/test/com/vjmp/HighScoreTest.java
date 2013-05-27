package com.vjmp;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.vjmp.utilities.HighScore;

public class HighScoreTest {
	HighScore hs = null;
	
	
	@Before
	public void setUp(){
		hs = new HighScore("chaptername");
	}
	@Test
	public void testStartStopInteger() {
	
		try {
			hs.start();
			Thread.sleep(1000);
			hs.stop();
			Assert.assertEquals("1.0", hs.toString());
			
			hs.start();
			Thread.sleep(2000);
			hs.stop();
			Assert.assertEquals("2.0", hs.toString());
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch bloc
		}
	
	}
	@Test
	public void testStartStopFloat(){
			
			try {
				hs.start();
				Thread.sleep(1500);
				hs.stop();
				Assert.assertEquals("1.500", hs.toString());
				
				hs.start();
				Thread.sleep(2555);
				hs.stop();
				Assert.assertEquals("2.555", hs.toString());
				
			} catch (InterruptedException e) {
			
			}
		
	}
	
	@Test
	public void testFinalScore(){
		hs = new HighScore("test","2.553");
		Assert.assertEquals("2.553",hs.toString());
		
	}
	
	@Test
	public void compareTest(){
		HighScore hs1 = new HighScore("test","2.010");
		HighScore hs2 = new HighScore("test","3.122");
		
		Assert.assertEquals(1, hs2.compareTo(hs1));
		
		hs2 = new HighScore("test","1.555");
		Assert.assertEquals(-1, hs2.compareTo(hs1));
		
		hs2 = new HighScore("test","2.010");
		Assert.assertEquals(0,hs2.compareTo(hs1));
}

}
