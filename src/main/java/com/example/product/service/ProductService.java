package com.example.product.service;

import java.util.List;

import com.example.core.exception.ServiceException;
import com.example.product.Product;
import com.example.product.repository.IProductRepository;

import io.github.dependency4j.Managed;
import io.github.dependency4j.Pull;
import io.github.dependency4j.util.StrUtil;

@Managed
public class ProductService {

	private IProductRepository productRepository;
	
	public @Pull ProductService(IProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public Product createProduct(Product product) {
		/* validate product before */
		return productRepository.createProduct(product);
	}
	
	public Product updateProduct(Product product) {
		/* validate product before */
		return productRepository.updateProduct(product);
	}
	
	public List<Product> findProductsByName(String name) {
		if (!StrUtil.isNullOrBlank(name))
			return productRepository.findProductsByName(name);
		
		throw new ServiceException("The name field was not filled.");
	}
	
}
