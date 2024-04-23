package com.example.inventory;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.example.product.Product;
import com.example.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(
	name = "inventory"
)
@NamedQueries(
		@NamedQuery(name = "Inventory_ClearInventory", query = "DELETE FROM Inventory")
)
public class Inventory {
	
	@Id
	@Column(
		name = "inventory_id",
		nullable = false
	)
	@GeneratedValue(
		strategy = GenerationType.IDENTITY
	)
	private Long id;
	
	@ManyToOne(
		fetch = FetchType.EAGER
	)
	@JoinColumn(
		name = "product_id",
		referencedColumnName = "product_id",
		nullable = false
	)
	private Product product;
	
	@Column(
		name = "amount",
		nullable = false
	)
	private Double amount;
	
	@Enumerated(EnumType.STRING)
	@Column(
		name = "type",
		nullable = false
	)
	private InventoryMovementType inventoryMovementType;

	@Column(
		name = "date",
		nullable = false,
		columnDefinition = "timestamp"
	)
	private LocalDateTime inventoryDateTime;
	
	@ManyToOne
	@JoinColumn(
		name = "user_id",
		referencedColumnName = "user_id",
		nullable = false
	)
	private User user;

	@Override
	public String toString() {
		return "Inventory [id=" + id + ", product=" + product + ", amount=" + amount + ", inventoryMovementType="
				+ inventoryMovementType + ", inventoryDateTime=" + inventoryDateTime + ", user=" + user + "]";
	}
	
}
