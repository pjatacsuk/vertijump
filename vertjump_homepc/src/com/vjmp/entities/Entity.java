package com.vjmp.entities;


public class Entity {
	public enum EntityType{BLOCK,TRIGGER};
	protected EntityType entityType;
	
	public EntityType getType() {
		return entityType;
	}
}
