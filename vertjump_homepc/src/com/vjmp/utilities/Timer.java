package com.vjmp.utilities;

/**
 * Sima id�zit� implement�ci�
 *
 */
public class Timer {
	private long startTime = 0;
	private boolean isStarted = false;
	private long msToSpend = 0;
	
	
	/**
	 * Konstruktor
	 * @param msToSpend : long - a m�rni kiv�nt id� milisecundumban
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
	 * Vizsg�ljuk mikor telt-e m�r le a timer
	 * @return ret : boolean -  true - ha letelt a timer, false - ha m�g megy
	 */
	public boolean isReady() {
		if(msToSpend < (System.currentTimeMillis() - startTime)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
}
