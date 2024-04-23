package com.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.inventory.Inventory;
import com.example.inventory.InventoryMovementType;
import com.example.inventory.service.InventoryService;
import com.example.product.Product;
import com.example.product.service.ProductService;
import com.example.user.User;
import com.example.user.service.UserService;

import io.github.dependency4j.Managed;
import io.github.dependency4j.Pull;

@Managed
public class EntityBasicSetup {

	private @Pull ProductService productService;
	private @Pull InventoryService inventoryService;
	private @Pull UserService userService;
	
	public EntityBasicSetup() {}

	public void setupBasicEntities() {

		User managerUser = userService.createUser("MANAGER", "123");
		User dummyUser = userService.createUser("DUMMY", "123");
		
		Product product = productService.createProduct(Product
				.builder()
					.name("Book")
					.costPrice(BigDecimal.valueOf(10))
					.sellPrice(BigDecimal.valueOf(25))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("PEN")
					.costPrice(BigDecimal.valueOf(10))
					.sellPrice(BigDecimal.valueOf(25))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("PENCIL")
					.costPrice(BigDecimal.valueOf(10))
					.sellPrice(BigDecimal.valueOf(25))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("HAMER")
					.costPrice(BigDecimal.valueOf(10))
					.sellPrice(BigDecimal.valueOf(25))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("OTHER PRODUCT")
					.costPrice(BigDecimal.valueOf(10))
					.sellPrice(BigDecimal.valueOf(25))
				.build());
		
		Inventory inventory = Inventory
				.builder()
					.product(product)
					.inventoryMovementType(InventoryMovementType.INPUT)
					.inventoryDateTime(LocalDateTime.now().minusMonths(2L))
					.user(managerUser)
					.amount(10d)
				.build();
		
		inventory = inventoryService.createInventoryMovement(inventory);
		
		Inventory inventory1 = Inventory
				.builder()
					.product(product)
					.inventoryMovementType(InventoryMovementType.INPUT)
					.inventoryDateTime(LocalDateTime.now())
					.user(dummyUser)
					.amount(30d)
				.build();
		
		inventory1 = inventoryService.createInventoryMovement(inventory1);
		
		Inventory inventory2 = Inventory
				.builder()
					.product(product)
					.inventoryMovementType(InventoryMovementType.OUTPUT)
					.inventoryDateTime(LocalDateTime.now())
					.user(managerUser)
					.amount(15d)
				.build();
		
		inventory2 = inventoryService.createInventoryMovement(inventory2);
		

	}

}
