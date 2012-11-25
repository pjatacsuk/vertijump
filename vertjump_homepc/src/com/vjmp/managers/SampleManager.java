package com.vjmp.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.vjmp.sound.Sample;

/**
 * A {@link Sample}-ket kezelõ manager. Lejátsza,Pause-olja,rendezi,frissiti, stb.. ezeket.
 * @author User
 *
 */


//Nem extendolja az EntityManager-t, mivel most util.map-et hasznalunk majd
public class SampleManager {
	protected Map<String,Sample> map = null;
	
	/**
	 * Konstruktor
	 */
	public SampleManager() {
		map = Collections.synchronizedMap(new HashMap<String,Sample>());
		
	}
	
	/**
	 * Hozzáad egy {@link Sample}-t a listához
	 * @param sample : {@link Sample} - a hozzáadandó {@link Sample}
	 */
	public void add(Sample sample) {
		map.put(sample.getName(),sample);
	}
	
	/**
	 * Remove-ol egy {@link Sample}-t a neve alapján a listábal
	 * @param name : {@link String} - az eltávolitandó sample neve
	 */
	public void remove(String name) {
		map.remove(name);
	}
	
	/**
	 * Visszatér a name által meghatározott {@link Sample}-el
	 * @param name : {@link String} - a {@link Sample} neve.
	 */
	public Sample get(String name) {
		return map.get(name);
		
	}
	
	/**
	 * Lejátsza az adott nevû {@link Sample}-t.
	 * @param name : {@link String} - a lejátszandó {@link Sample} neve.
	 */
	public void playSample(String name){
		map.get(name).play();
	}
	
	/**
	 * Lepause-olja az adott nevû {@link Sample}-t.
	 * @param name : {@link String} - a pause-olandó {@link Sample} neve.
	 */
	public void pauseSample(String name){
		map.get(name).pause();
	}
	
	/**
	 * Unpause-olja az adott nevû {@link Sample}-t
	 * @param name : {@link String} - az unpause-olandó {@link Sample} neve.
	 */
	public void unpauseSample(String name){
		map.get(name).unpause();
	}
	
}
