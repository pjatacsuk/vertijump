package com.vjmp.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.vjmp.sound.Sample;




//Nem extendolja az EntityManager-t, mivel most util.map-et hasznalunk majd
public class SampleManager {
	protected Map<String,Sample> map = null;
	
	public SampleManager() {
		map = Collections.synchronizedMap(new HashMap<String,Sample>());
		
	}
	
	public void add(Sample sample) {
		map.put(sample.getName(),sample);
	}
	public void remove(String name) {
		map.remove(name);
	}
	public void get(String name) {
		map.get(name);
	}
	public void playSample(String name){
		map.get(name).play();
	}
	public void pauseSample(String name){
		map.get(name).pause();
	}
	public void unpauseSample(String name){
		map.get(name).unpause();
	}
	
}
