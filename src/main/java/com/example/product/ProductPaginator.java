package com.example.product;

import com.example.core.Paginator;
import com.example.product.repository.IProductRepository;

import io.github.dependency4j.Managed;
import io.github.dependency4j.Pull;

@Managed(dynamic = true)
public class ProductPaginator implements Paginator<Product> {

	private long paginatorSize;
	private int paginatorOffset;
	
	private Product currentProduct;

	private final IProductRepository productRepository;
	
	@Pull
	public ProductPaginator(IProductRepository productRepository) {
		this.productRepository = productRepository;
		update();
	}
	
	@Override
	public void update() {
		paginatorSize = productRepository.countProducts() - 1;
		retrieveCurrentProductFromPaginator(); 
	}
	
	@Override
	public void next() {

		paginatorOffset 
			= (int) Math.min(paginatorOffset + 1, paginatorSize);
		
		retrieveCurrentProductFromPaginator();
	}

	@Override
	public void previous() {

		paginatorOffset 
			= (int) Math.max(paginatorOffset - 1, 0);

		retrieveCurrentProductFromPaginator();
	}
	
	private void retrieveCurrentProductFromPaginator() {
		currentProduct = productRepository.retrieveProductFromPaginator(this);
	}
	
	@Override
	public Product getCurrent() {
		return currentProduct;
	}

	@Override
	public int getOffset() {
		return paginatorOffset;
	}

	@Override
	public long getSize() {
		return paginatorSize;
	}

	@Override
	public String toString() {
		return "ProductPaginator [paginatorSize=" + paginatorSize + ", paginatorOffset=" + paginatorOffset + "]";
	}
	
}
