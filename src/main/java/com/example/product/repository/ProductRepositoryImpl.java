package com.example.product.repository;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.example.core.Paginator;
import com.example.core.support.EntityManagerRepository;
import com.example.product.Product;

import io.github.dependency4j.Managed;

@Managed(name = "EntityManagerProductRepository")
public class ProductRepositoryImpl extends EntityManagerRepository implements IProductRepository { 

	@Override
	public List<Product> findProductsByName(String name) {
		TypedQuery<Product> selectProductQuery = 
				entityManager.createNamedQuery("Product_FindByName", Product.class);

		selectProductQuery.setParameter("paramName", name);
		return selectProductQuery.getResultList();
	}

	@Override
	public Product retrieveProductFromPaginator(Paginator<Product> paginator) {
		Query selectProductPageQuery = 
				entityManager.createNativeQuery("SELECT DISTINCT * FROM products LIMIT 1 OFFSET :paramOffset", Product.class);
		
		selectProductPageQuery.setParameter("paramOffset", paginator.getOffset());
		
		return (Product) selectProductPageQuery.getSingleResult();
	}
	
	@Override
	public long countProducts() {
		Query countProductsQuery = entityManager.createNamedQuery("Product_Count");
		return ((Long)countProductsQuery.getSingleResult());
	}
	
	@Override
	public Product createProduct(Product product) {
		return executetransactionedMerge(product);
	}

	@Override
	public Product updateProduct(Product product) {
		/* could have a different update approach tho */
		return executetransactionedMerge(product);
	}

	@Override
	public List<Product> findAllProducts() {
		TypedQuery<Product> selectProductQuery = 
				entityManager.createQuery("FROM Product", Product.class);
		return selectProductQuery.getResultList();
	}
	
} 
