package com.example.inventory.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.example.inventory.Inventory;
import com.example.inventory.InventoryFilterOptions;
import com.example.inventory.InventorySummary;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.transformer.InventoryResponseTransformer;
import com.example.ui.UIMessages;

import io.github.dependency4j.Managed;
import io.github.dependency4j.Pull;

@Managed
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	private final InventorySummary inventorySummary;
	private final InventoryResponseTransformer inventoryResponseTransformer;
	private final UIMessages uiMessages;
	
	public @Pull InventoryService(InventoryRepository inventoryRepository, 
								  InventorySummary inventorySummary,
								  InventoryResponseTransformer inventoryResponseTransformer,
								  UIMessages uiMessages) 
	{
		this.inventoryRepository = inventoryRepository;
		this.inventorySummary = inventorySummary;
		this.inventoryResponseTransformer = inventoryResponseTransformer;
		this.uiMessages = uiMessages;
	}
	
	public List<InventoryResponse> fetchInventoriesFromFilterOptions(InventoryFilterOptions filterOptions) {
		if (filterOptions.getStartDateTime().isAfter(filterOptions.getEndDateTime())) {
			uiMessages.displayWarning("The start date must not be greater than the end date.", "Validation error");
			return Collections.emptyList();
		}
		
		return inventoryRepository
				.fetchInventoriesFromFilterOptions(filterOptions).stream()
				.map(inventory 
						-> inventoryResponseTransformer.transformInventoryToInventoryResponse(inventory))
				.collect(Collectors.toList());
	}
	
	public String createSummaryRichText(List<InventoryResponse> inventoryResponseList) {
		StringBuilder builder = new StringBuilder();
		
		Map<String, Object> summaryMap = inventorySummary
				.createInventorySummary(inventoryResponseList); 
		
		builder.append("<html>");
		for(Map.Entry<String, Object> entries : summaryMap.entrySet())
			builder.append(" %s = <b>%s</b> "
					.formatted(entries.getKey(), entries.getValue()));

		builder.append("</html>");
		
		return builder.toString().strip();
	}
	
	private boolean validatePotentiallyDestructiveActionBefore(String message) {

		final int option = JOptionPane.showConfirmDialog(null, 
				message, 
				"Potentially destructive action", 
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.WARNING_MESSAGE);
		
		return option == JOptionPane.YES_OPTION;
	}
	
	public void clearInventory() {
		
		if (!validatePotentiallyDestructiveActionBefore("Are you sure you want to clear all inventories?")) {
			uiMessages.displayInfo("Cancelled.");
			return;
		}

		inventoryRepository.clearInventory();
	}
	
	public void clearInventoryById(Long inventoryId) {

		if (!validatePotentiallyDestructiveActionBefore("Are you sure you want to clear the selected inventory?")) {
			uiMessages.displayInfo("Cancelled.");
			return;
		}

		Inventory inventory = inventoryRepository.findInventoryById(inventoryId);
		inventoryRepository.removeInventory(inventory);
	}


	public Inventory createInventoryMovement(Inventory inventory) {
		return inventoryRepository.createInventoryMovement(inventory);
	}
	
	public Inventory findInventoryById(Long inventoryId) {
		return inventoryRepository.findInventoryById(inventoryId);
	}

}
