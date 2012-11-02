package com.vjmp.utilities;

public class StopWatch {
	private long timeSpent = 0;
	private long startTime = 0;
	private boolean running = false;
	
	public void start() {
		startTime = System.currentTimeMillis();
		running = true;
	}
	public void stop() {
		timeSpent = System.currentTimeMillis() - startTime;
		running = false;
	}
	public long getTime() {
		if(running) {
		timeSpent = System.currentTimeMillis() - startTime;
		}
		return timeSpent;
	}
	public void reset() {
		startTime = 0;
		timeSpent = 0;
		
	}
	
}
