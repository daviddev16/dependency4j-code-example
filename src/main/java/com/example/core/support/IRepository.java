package com.example.core.support;

import javax.persistence.EntityManager;

public interface IRepository<I, T> {

	EntityManager getEntityManager();
	
}
