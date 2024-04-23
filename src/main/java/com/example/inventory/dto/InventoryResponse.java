package com.example.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.inventory.InventoryMovementType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {

	private Long inventoryId;
	
	private String productName;

	private BigDecimal unitCostPrice;

	private BigDecimal unitSellPrice;

	private double amount;

	private BigDecimal total;
	
	private LocalDateTime inventoryDateTime;

	private String userName;

	private InventoryMovementType inventoryMovementType;
}
