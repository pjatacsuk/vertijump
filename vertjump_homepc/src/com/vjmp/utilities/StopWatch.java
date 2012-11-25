package com.vjmp.utilities;

/**
 * Sima stopper implementáció
 *
 */
public class StopWatch {
	private long timeSpent = 0;
	private long startTime = 0;
	private boolean running = false;
	
	/**
	 * Eliniditja a stoppert.
	 */
	public void start() {
		startTime = System.currentTimeMillis();
		running = true;
	}
	
	/**
	 * Megállitja a stoppert.
	 */
	public void stop() {
		timeSpent = System.currentTimeMillis() - startTime;
		running = false;
	}
	
	/**
	 * Visszatér a mért idõvel
	 * @return timeSpent : int - mért idõ
	 */
	public long getTime() {
		if(running) {
		timeSpent = System.currentTimeMillis() - startTime;
		}
		return timeSpent;
	}
	
	/**
	 * Reseteli a stoppert.
	 */
	public void reset() {
		startTime = 0;
		timeSpent = 0;
		
	}
	
}
