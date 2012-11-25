package com.vjmp.managers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.vjmp.sound.Sample;

/**
 * A {@link Sample}-ket kezel� manager. Lej�tsza,Pause-olja,rendezi,frissiti, stb.. ezeket.
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
	 * Hozz�ad egy {@link Sample}-t a list�hoz
	 * @param sample : {@link Sample} - a hozz�adand� {@link Sample}
	 */
	public void add(Sample sample) {
		map.put(sample.getName(),sample);
	}
	
	/**
	 * Remove-ol egy {@link Sample}-t a neve alapj�n a list�bal
	 * @param name : {@link String} - az elt�volitand� sample neve
	 */
	public void remove(String name) {
		map.remove(name);
	}
	
	/**
	 * Visszat�r a name �ltal meghat�rozott {@link Sample}-el
	 * @param name : {@link String} - a {@link Sample} neve.
	 */
	public Sample get(String name) {
		return map.get(name);
		
	}
	
	/**
	 * Lej�tsza az adott nev� {@link Sample}-t.
	 * @param name : {@link String} - a lej�tszand� {@link Sample} neve.
	 */
	public void playSample(String name){
		map.get(name).play();
	}
	
	/**
	 * Lepause-olja az adott nev� {@link Sample}-t.
	 * @param name : {@link String} - a pause-oland� {@link Sample} neve.
	 */
	public void pauseSample(String name){
		map.get(name).pause();
	}
	
	/**
	 * Unpause-olja az adott nev� {@link Sample}-t
	 * @param name : {@link String} - az unpause-oland� {@link Sample} neve.
	 */
	public void unpauseSample(String name){
		map.get(name).unpause();
	}
	
}
