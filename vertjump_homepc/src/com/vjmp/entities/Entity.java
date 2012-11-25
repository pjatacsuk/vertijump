package com.vjmp.entities;


/**
 * A fõ entitás osztálya. Belõle származik minden fajta entitás. (pl. DrawableEntity,TriggerEntity stb).
 */
public class Entity {
	public enum EntityType{BLOCK,TRIGGER};
	protected EntityType entityType;
	
	/**
	 * Visszaadja az entitás tipusát
	 * @return entityType : {@link EntityType} - az entitás tipusa
	 */
	public EntityType getType() {
		return entityType;
	}
}
