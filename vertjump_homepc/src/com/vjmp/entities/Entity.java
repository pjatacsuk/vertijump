package com.vjmp.entities;


public class Entity {
	public enum EntityType{BLOCK,FINISH_LINE,SPAWN_POINT};
	protected EntityType entityType;
	
	protected EntityType getType() {
		return entityType;
	}
}
