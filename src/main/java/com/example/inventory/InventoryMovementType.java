package com.example.inventory;

import com.example.core.IconNameHolder;

public enum InventoryMovementType { 

	OUTPUT("down", "OUT"), 
	INPUT ("up",   "IN");
	
	private final IconNameHolder iconNameHolder;
	private final String alias;
	
	private InventoryMovementType(String iconName, String alias) {
		this.iconNameHolder = new IconNameHolder(iconName);
		this.alias = alias;
	}
	
	public static InventoryMovementType getInventoryMovementTypeByAlias(String alias) {
		for (InventoryMovementType movementType : values()) {
			if (movementType.getAlias().equals(alias))
				return movementType;
		}
		return null;
	}
	
	public IconNameHolder getIconNameHolder() {
		return iconNameHolder;
	}
	
	public String getAlias() {
		return alias;
	}
	
	@Override
	public String toString() {
		return alias;
	}
	
}
