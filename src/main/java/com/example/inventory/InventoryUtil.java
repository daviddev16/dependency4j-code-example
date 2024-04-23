package com.example.inventory;

import java.math.BigDecimal;

public final class InventoryUtil {

	public static double negateWhenOutput(double value, InventoryMovementType inventoryMovementType) {
		return negateWhenOutput(new BigDecimal(value), inventoryMovementType).doubleValue();
	}
	
	public static BigDecimal negateWhenOutput(BigDecimal value, InventoryMovementType inventoryMovementType) {
		return (inventoryMovementType == InventoryMovementType.OUTPUT) ? value.negate() : value;
	}
	
}
