package com.example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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

	private static final Random RANDOM = new Random();
	
	private @Pull ProductService productService;
	private @Pull InventoryService inventoryService;
	private @Pull UserService userService;
	
	public EntityBasicSetup() {}

	public void setupBasicEntities() {
		createBasicUsers();
		createBasicProducts();
		generateRandomInventoryData(50);
	}
	
	private void createBasicUsers() {
		userService.createUser("MANAGER", "123");
		userService.createUser("DUMMY", "123");
	}
	 
	private void createBasicProducts() {
		
		productService.createProduct(Product
				.builder()
					.name("CLEAN CODE BOOK")
					.costPrice(BigDecimal.valueOf(100))
					.sellPrice(BigDecimal.valueOf(160))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("RED PEN")
					.costPrice(BigDecimal.valueOf(1))
					.sellPrice(BigDecimal.valueOf(5.5))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("BLACK LINUX NOTEBOOK")
					.costPrice(BigDecimal.valueOf(10))
					.sellPrice(BigDecimal.valueOf(25))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("JAVA COFFEE NICE 1kg")
					.costPrice(BigDecimal.valueOf(50))
					.sellPrice(BigDecimal.valueOf(80))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("ENERGY DRINK 437ml")
					.costPrice(BigDecimal.valueOf(5))
					.sellPrice(BigDecimal.valueOf(12))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("WHITE T-SHIRT")
					.costPrice(BigDecimal.valueOf(100))
					.sellPrice(BigDecimal.valueOf(160))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("JEANS")
					.costPrice(BigDecimal.valueOf(200))
					.sellPrice(BigDecimal.valueOf(550))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("WOOD TABLE")
					.costPrice(BigDecimal.valueOf(100))
					.sellPrice(BigDecimal.valueOf(350))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("RED T-SHIRT")
					.costPrice(BigDecimal.valueOf(50))
					.sellPrice(BigDecimal.valueOf(180))
				.build());
		
		productService.createProduct(Product
				.builder()
					.name("DUCT TAPE")
					.costPrice(BigDecimal.valueOf(10))
					.sellPrice(BigDecimal.valueOf(27))
				.build());
	}
	
	private void generateRandomInventoryData(int count) {
		List<User> users = userService.findAllUsers();
		List<Product> products = productService.findAllProducts();
		for (int i = 0; i < count; i++) {
			Inventory inventory = Inventory
					.builder()
						.amount(pickRandomAmount())
						.inventoryDateTime(pickRandomLocalDateTime())
						.inventoryMovementType(pickRandomInventoryMovement())
						.product(pickRandomProduct(products))
						.user(pickRandomUser(users))
					.build();
			inventoryService.createInventoryMovement(inventory);
		}
	}
	
	private LocalDateTime pickRandomLocalDateTime() {
		return randomFromGenericList(List
				.of(LocalDateTime.now(), 
					LocalDateTime.now().minusMonths(1),
					LocalDateTime.now().minusMonths(2),
					LocalDateTime.now().minusMonths(3)));
	}
	
	private <T> T randomFromGenericList(List<T> genericList) {
		return genericList.get(RANDOM.nextInt(genericList.size()));
	}
	
	private InventoryMovementType pickRandomInventoryMovement() {
		return randomFromGenericList(List
				.of(InventoryMovementType.INPUT, InventoryMovementType.OUTPUT));
	}
	
	private Double pickRandomAmount() {
		return randomFromGenericList(List
				.of(10d, 15d, 20d, 30d, 50d, 70d, 5d, 1d, 90d));
	}
	
	private User pickRandomUser(List<User> users) {
		return randomFromGenericList(users);
	}
	
	private Product pickRandomProduct(List<Product> product) {
		return randomFromGenericList(product);
	}

}
