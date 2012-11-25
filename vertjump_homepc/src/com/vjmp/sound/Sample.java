package com.vjmp.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.vjmp.sound.Sample.PlayMode;

/**
 * A {@link Sample} class felel�s az adott audio elemek kezel�s�rt, bet�lt�s ut�n lej�tszhat�ak, pauseolhat�k,
 * loopolhatok, egyszeri lej�tsz�sra is be�llithat�
 * @author User
 *
 */
public class Sample {
	public static enum Volume {
		LOW, MEDIUM, HIGH, MUTE
	};
	public static enum PlayMode {
		ONCE,LOOP
	}
	private Clip clip;
	private Volume volume = Volume.MUTE;
	private PlayMode playMode = PlayMode.ONCE;
	private String name = null;
	
	
	/**
	 * Konstruktor
	 * @param name : {@link String} - adott n�v 
	 * @param path : {@link String} - adott el�r�si �tvonal
	 */
	public  Sample(String name,String path) {
		try {
			this.name = name;
			
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			volume = Volume.MEDIUM;
			
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	* @param name : {@link String} - adott n�v 
	 * @param path : {@link String} - adott el�r�si �tvonal
	 * @param playMode : {@link PlayMode} - adott playMode(ONCE,LOOP)
	 */
	 public Sample(String name, String path, PlayMode playMode) {
		 try {
				this.name = name;
				
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				volume = Volume.MEDIUM;
				this.playMode = playMode;
				
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 	
	}

	 /**
	  * Be�llitja a {@link Sample} hangerej�t az adott hanger�re.
	  * @param volume : {@link Volume} - az adott hanger�
	  */
	public void setVolume(Volume volume){
		 this.volume = volume;
	 }
	
	/**
	 * Elinditja a lej�tsz�st
	 */
	 public void play() {
	      if (volume != Volume.MUTE) {
	    	  if(playMode == PlayMode.ONCE) {
		    	  if(!clip.isRunning()) {
		    		  
		    		  clip.setFramePosition(0);
		    		  clip.start();
		    	  }
	    	  } else if(playMode == PlayMode.LOOP) {
	    		  clip.loop(clip.LOOP_CONTINUOUSLY);
	    	  }
	      }
	      
	 }	
	 
	 /**
	  * Meg�llitja a lej�tsz�st
	  */
	 public void stop() {
		 if(clip.isRunning()) {
			 clip.stop();
			 clip.setFramePosition(0);
		 }
	 }
	 
	 /**
	  * Pauseolja a lej�tsz�st
	  */
	 public void pause(){
		 if(clip.isRunning()){
			 clip.stop();
		 }
	 }
	 
	 /**
	  * Unpause-olja a lej�tsz�st
	  */
	 public void unpause(){
		 if(!clip.isRunning()){
			 clip.start();
		 }
			 
	 }
	 
	 /**
	  * Len�mitja a lej�tsz�st
	  */
	public void mute() {
		volume = Volume.MUTE;
	}
	
	/**
	 * Unmute-olja a lej�tsz�st
	 */
	public void unmute() {
		volume = Volume.MEDIUM;
	}
	
	/**
	 * Vissat�r a sample nev�vel
	 * @return name : {@link String} a sample neve.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Be�llitja a sample {@link PlayMode}-j�t a megadott {@link PlayMode} alapj�n.
	 * @param playMode : {@link PlayMode} - a megadott {@link PlayMode}.
	 */
	public void setPlayMode(PlayMode playMode) {
		this.playMode = playMode;
	}
}
