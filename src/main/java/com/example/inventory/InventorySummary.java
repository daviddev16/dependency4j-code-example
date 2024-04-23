package com.example.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.inventory.dto.InventoryResponse;

import io.github.dependency4j.Managed;

@Managed
public final class InventorySummary {

	public Map<String, Object> createInventorySummary(List<InventoryResponse> inventoryResponseList) {
		Map<String, Object> summaryMap = new HashMap<String, Object>();
		
		summaryMap.put("Total Stock Amount", 
				getSumOfTotalAmount(inventoryResponseList));
		
		summaryMap.put("Total Stock Inputs", 
				countStockByInventoryType(inventoryResponseList, InventoryMovementType.INPUT));
		
		summaryMap.put("Total Stock Outputs", 
				countStockByInventoryType(inventoryResponseList, InventoryMovementType.OUTPUT));
		
		return summaryMap;
	}
	
	private long countStockByInventoryType(List<InventoryResponse> inventoryResponseList, 
												  InventoryMovementType inventoryMovementType) {
		return inventoryResponseList.stream()
				.filter(inventoryResponse -> inventoryResponse
						.getInventoryMovementType() == inventoryMovementType)
				.count();
	}
	
	private double getSumOfTotalAmount(List<InventoryResponse> inventoryResponseList) {
		double amountSum = 0;
		for (InventoryResponse inventoryResponse : inventoryResponseList)
			amountSum += InventoryUtil.negateWhenOutput(inventoryResponse.getAmount(), 
					inventoryResponse.getInventoryMovementType());

		return amountSum;
	}
}
