package com.example.inventory.repository;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.example.core.Transactioned;
import com.example.core.support.EntityManagerRepository;
import com.example.inventory.Inventory;
import com.example.inventory.InventoryFilterOptions;

import io.github.dependency4j.Managed;

@Managed(name = "EntityManagerInventoryRepository")
public class InventoryRepositoryImpl extends EntityManagerRepository implements InventoryRepository { 

	@Override
	public List<Inventory> fetchInventoriesFromFilterOptions(InventoryFilterOptions filterOptions) {
		entityManager.clear();
		
		StringBuilder queryStringBuilder = new StringBuilder()
				.append("FROM Inventory i ")
				.append("WHERE ")
				.append("i.inventoryDateTime BETWEEN :paramStartDate AND :paramEndDate");
		
		if (filterOptions.hasInventoryMovementTypeFilter()) 
			queryStringBuilder.append(" AND i.inventoryMovementType = :paramInventoryMovementType");
		
		if (filterOptions.hasUserLoginFilter()) 
			queryStringBuilder.append(" AND i.user.login = :paramLogin");
		
		TypedQuery<Inventory> selectInventoriesQuery = 
				entityManager.createQuery(queryStringBuilder.toString(), Inventory.class);
		
		selectInventoriesQuery.setParameter("paramStartDate", filterOptions.getStartDateTime());
		selectInventoriesQuery.setParameter("paramEndDate", filterOptions.getEndDateTime());
		
		if (filterOptions.hasInventoryMovementTypeFilter()) 
			selectInventoriesQuery.setParameter("paramInventoryMovementType", 
					filterOptions.getInventoryMovementType());

		if (filterOptions.hasUserLoginFilter()) 
			selectInventoriesQuery.setParameter("paramLogin", filterOptions.getUserLogin());
		
		return selectInventoriesQuery.getResultList();
	}

	@Override
	public void clearInventory() {
		executeTransactionedQuery(entityManager -> 
		{
			Query clearInventoryQuery = 
					entityManager.createNamedQuery("Inventory_ClearInventory");
			
			clearInventoryQuery.executeUpdate();
			
			return Transactioned.EMPTY_TRANSACTIONED;
		});
		
		entityManager.clear();
	}
	
	@Override
	public Inventory createInventoryMovement(Inventory inventory) {
		/* do some optional validation */
		return executetransactionedMerge(inventory);	
	}

	@Override
	public void removeInventory(Inventory inventory) {
		executeTransactionedQuery(entityManager -> 
		{
			entityManager.remove(inventory);
			return Transactioned.EMPTY_TRANSACTIONED;
		});
	}

	@Override
	public Inventory findInventoryById(Long inventoryId) {
		return entityManager.find(Inventory.class, inventoryId);
	}

} 
