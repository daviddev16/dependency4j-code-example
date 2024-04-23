package com.example.inventory;

import java.time.LocalDateTime;

import io.github.dependency4j.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryFilterOptions {

	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private InventoryMovementType inventoryMovementType;
	private String userLogin;
	
	public boolean hasInventoryMovementTypeFilter() {
		return inventoryMovementType != null;
	}

	public boolean hasUserLoginFilter() {
		return !StrUtil.isNullOrBlank(userLogin);
	}

}
