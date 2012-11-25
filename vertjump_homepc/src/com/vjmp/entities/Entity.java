package com.vjmp.entities;


/**
 * A f� entit�s oszt�lya. Bel�le sz�rmazik minden fajta entit�s. (pl. DrawableEntity,TriggerEntity stb).
 */
public class Entity {
	public enum EntityType{BLOCK,TRIGGER};
	protected EntityType entityType;
	
	/**
	 * Visszaadja az entit�s tipus�t
	 * @return entityType : {@link EntityType} - az entit�s tipusa
	 */
	public EntityType getType() {
		return entityType;
	}
}
