package com.vjmp;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import com.vjmp.utilities.Timer;
import org.junit.Before;
import org.junit.Test;
;

public class TimerTest {
	Timer timer = null;
	
	
	@Before
	public void setUp(){
		timer = new Timer(1000);
	}
	@Test
	public void testOne() {
		timer.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		Assert.assertEquals(false,timer.isReady());
	}
	@Test
	public void testReady(){
		timer.start();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		Assert.assertEquals(true, timer.isReady());
	}

}
