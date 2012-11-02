package com.vjmp;

public class Timer {
	private long startTime = 0;
	private boolean isStarted = false;
	private long msToSpend = 0;
	
	public Timer(long msToSpend){
		this.msToSpend = msToSpend;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
		isStarted = true;
	}
	public boolean isReady() {
		if(msToSpend < (System.currentTimeMillis() - startTime)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
}
