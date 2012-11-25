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
 * A {@link Sample} class felelõs az adott audio elemek kezelésért, betöltés után lejátszhatóak, pauseolhatók,
 * loopolhatok, egyszeri lejátszásra is beállitható
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
	 * @param name : {@link String} - adott név 
	 * @param path : {@link String} - adott elérési útvonal
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
	* @param name : {@link String} - adott név 
	 * @param path : {@link String} - adott elérési útvonal
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
	  * Beállitja a {@link Sample} hangerejét az adott hangerõre.
	  * @param volume : {@link Volume} - az adott hangerõ
	  */
	public void setVolume(Volume volume){
		 this.volume = volume;
	 }
	
	/**
	 * Elinditja a lejátszást
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
	  * Megállitja a lejátszást
	  */
	 public void stop() {
		 if(clip.isRunning()) {
			 clip.stop();
			 clip.setFramePosition(0);
		 }
	 }
	 
	 /**
	  * Pauseolja a lejátszást
	  */
	 public void pause(){
		 if(clip.isRunning()){
			 clip.stop();
		 }
	 }
	 
	 /**
	  * Unpause-olja a lejátszást
	  */
	 public void unpause(){
		 if(!clip.isRunning()){
			 clip.start();
		 }
			 
	 }
	 
	 /**
	  * Lenémitja a lejátszást
	  */
	public void mute() {
		volume = Volume.MUTE;
	}
	
	/**
	 * Unmute-olja a lejátszást
	 */
	public void unmute() {
		volume = Volume.MEDIUM;
	}
	
	/**
	 * Vissatér a sample nevével
	 * @return name : {@link String} a sample neve.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Beállitja a sample {@link PlayMode}-ját a megadott {@link PlayMode} alapján.
	 * @param playMode : {@link PlayMode} - a megadott {@link PlayMode}.
	 */
	public void setPlayMode(PlayMode playMode) {
		this.playMode = playMode;
	}
}
