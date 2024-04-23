package com.example.product.repository;

import java.util.List;

import com.example.core.Paginator;
import com.example.product.Product;

public interface IProductRepository {

	Product createProduct(Product product);
	
	Product updateProduct(Product product);
	
	List<Product> findProductsByName(String name);
	
	Product retrieveProductFromPaginator(Paginator<Product> paginator);
	
	long countProducts();
	
	
}
