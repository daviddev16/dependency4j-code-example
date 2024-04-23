package com.example.product;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.inventory.Inventory;

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
	name = "products"
)
@NamedQueries({
	@NamedQuery(
			name = "Product_FindByName", 
			query = "FROM Product p WHERE p.name LIKE '%' || :paramName || '%'"),
	@NamedQuery(
			name = "Product_Count", 
			query = "SELECT count(*) FROM Product")
})
public class Product {
	
	@Id
	@Column(
		name = "product_id",
		nullable = false
	)
	@GeneratedValue(
		strategy = GenerationType.IDENTITY
	)
	private Long id;
	
	@Column(
		name = "name",
		nullable = false
	)
	private String name;
	
	@Column(
		name = "sell_price",
		nullable = false
	)
	private BigDecimal sellPrice;
	
	@Column(
		name = "cost_price",
		nullable = false
	)
	private BigDecimal costPrice;
	
	@OneToMany(mappedBy = "product")
	private List<Inventory> inventories;

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", sellPrice=" + sellPrice + ", costPrice=" + costPrice + "]";
	}

}
