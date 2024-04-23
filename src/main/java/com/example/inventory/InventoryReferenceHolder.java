package com.example.inventory;

import com.example.core.IconNameHolder;

public class InventoryReferenceHolder extends IconNameHolder {

	private final Long inventoryId;
	
	public InventoryReferenceHolder(IconNameHolder parentIconNameHolder, Long inventoryId) {
		super(parentIconNameHolder.getIconName());
		this.inventoryId = inventoryId;
	}
	
	public InventoryReferenceHolder(String iconName, Long inventoryId) {
		super(iconName);
		this.inventoryId = inventoryId;
	}
	
	public Long getInventoryId() {
		return inventoryId;
	}
}
