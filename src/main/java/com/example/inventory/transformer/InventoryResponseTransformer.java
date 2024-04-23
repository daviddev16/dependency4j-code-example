package com.example.inventory.transformer;

import java.math.BigDecimal;

import com.example.inventory.Inventory;
import com.example.inventory.dto.InventoryResponse;

import io.github.dependency4j.Managed;

@Managed(disposable = false)
public class InventoryResponseTransformer {

	public InventoryResponse transformInventoryToInventoryResponse(Inventory inventory) {
		return InventoryResponse
				.builder()
					.inventoryId(inventory.getId())
					.amount(inventory.getAmount())
					.inventoryDateTime(inventory.getInventoryDateTime())
					.inventoryMovementType(inventory.getInventoryMovementType())
					.productName(inventory.getProduct().getName())
					.total(inventory.getProduct()
					.getSellPrice().multiply(new BigDecimal(inventory.getAmount())))
					.unitCostPrice(inventory.getProduct().getCostPrice())
					.unitSellPrice(inventory.getProduct().getSellPrice())
					.userName(inventory.getUser().getLogin())
				.build();
	}
	
}
