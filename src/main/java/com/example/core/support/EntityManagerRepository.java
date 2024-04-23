package com.example.core.support;

import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.example.core.Transactioned;

public abstract class EntityManagerRepository {

	protected final EntityManagerFactory entityManagerFactory = 
			Persistence.createEntityManagerFactory("EntityManagerRepositoryEM");
	
	protected final EntityManager entityManager = 
			entityManagerFactory.createEntityManager();
	
	@SuppressWarnings("unchecked")
	public <T> T executetransactionedMerge(T entity) {
		return (T) executeTransactionedQuery((entityManager) 
					-> Transactioned.of(entityManager.merge(entity)))
				.getTransactionEntityResult();
	}
	
	protected Transactioned<?> executeTransactionedQuery(Function<EntityManager, 
														 Transactioned<?>> transactionedFunction) {
		
		if (transactionedFunction == null)
			throw new NullPointerException("Transactioned function is null.");
		
		EntityTransaction entityTransaction = beginTransaction();
		
		try {
			return transactionedFunction.apply(entityManager);
		} catch (Exception exception) {
			handleTransactionException(exception, entityTransaction);
			throw exception;
		} finally {
			finalizeTransaction(entityTransaction);
		}
	}
	
	protected boolean isTransactionActive(EntityTransaction entityTransaction) {
		return entityTransaction != null && entityTransaction.isActive();
	}
	
	protected void handleTransactionException(Exception exception, 
											  EntityTransaction entityTransaction) {
		if (isTransactionActive(entityTransaction))
			entityTransaction.rollback();
	}
	
	protected void finalizeTransaction(EntityTransaction entityTransaction) {
		if (isTransactionActive(entityTransaction))
			entityTransaction.commit();
	}
	
	protected EntityTransaction beginTransaction() {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		return entityTransaction;
	}
	
}
