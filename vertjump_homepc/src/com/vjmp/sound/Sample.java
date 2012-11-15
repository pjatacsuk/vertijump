package com.vjmp.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.vjmp.sound.Sample.PlayMode;

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

	public void setVolume(Volume volume){
		 this.volume = volume;
	 }
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
	 public void stop() {
		 if(clip.isRunning()) {
			 clip.stop();
			 clip.setFramePosition(0);
		 }
	 }
	 public void pause(){
		 if(clip.isRunning()){
			 clip.stop();
		 }
	 }
	 public void unpause(){
		 if(!clip.isRunning()){
			 clip.start();
		 }
			 
	 }
	public void mute() {
		volume = Volume.MUTE;
	}
	public void unmute() {
		volume = Volume.MEDIUM;
	}
	public String getName() {
		return name;
	}
	public void setPlayMode(PlayMode playMode) {
		this.playMode = playMode;
	}
}
