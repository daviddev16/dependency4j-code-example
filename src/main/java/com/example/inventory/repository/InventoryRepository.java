package com.example.inventory.repository;


import java.util.List;

import com.example.inventory.Inventory;
import com.example.inventory.InventoryFilterOptions;

public interface InventoryRepository {

	Inventory createInventoryMovement(Inventory inventory);
	
	Inventory findInventoryById(Long inventoryId);
	
	List<Inventory> fetchInventoriesFromFilterOptions(InventoryFilterOptions filterOptions);
	
	void removeInventory(Inventory inventory);
	
	void clearInventory();
	
}
