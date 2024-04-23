package com.example.core;

public class Transactioned<T> {

	public static final Transactioned<Object> EMPTY_TRANSACTIONED = new Transactioned<Object>();  
	
	public static <T> Transactioned<T> of(T entity) { 
		return new Transactioned<T>(entity); 
	}
	
	private T transactionEntityResult;

	public Transactioned() {}
	
	public Transactioned(T transactionEntityResult) {
		this.transactionEntityResult = transactionEntityResult;
	}

	public T getTransactionEntityResult() {
		return transactionEntityResult;
	}

}
