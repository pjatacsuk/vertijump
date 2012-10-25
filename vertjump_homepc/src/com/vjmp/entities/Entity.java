package com.vjmp.entities;


public class Entity {
	public enum EntityType{BLOCK,FINISH_LINE,TRIGGER};
	protected EntityType entityType;
	
	public EntityType getType() {
		return entityType;
	}
}
