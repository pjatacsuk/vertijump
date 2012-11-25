package com.vjmp.utilities;

/**
 * Sima idõzitõ implementáció
 *
 */
public class Timer {
	private long startTime = 0;
	private boolean isStarted = false;
	private long msToSpend = 0;
	
	
	/**
	 * Konstruktor
	 * @param msToSpend : long - a mérni kivánt idõ milisecundumban
	 */
	public Timer(long msToSpend){
		this.msToSpend = msToSpend;
	}
	
	/**
	 * Elinditja a timer-t.
	 */
	public void start() {
		startTime = System.currentTimeMillis();
		isStarted = true;
	}
	
	/**
	 * Vizsgáljuk mikor telt-e már le a timer
	 * @return ret : boolean -  true - ha letelt a timer, false - ha még megy
	 */
	public boolean isReady() {
		if(msToSpend < (System.currentTimeMillis() - startTime)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
}
