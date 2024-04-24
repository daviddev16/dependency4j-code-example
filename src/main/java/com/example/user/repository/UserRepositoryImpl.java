package com.example.user.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.example.core.support.EntityManagerRepository;
import com.example.user.User;

import io.github.dependency4j.Managed;

@Managed(name = "EntityManagerUserRepository")
public class UserRepositoryImpl extends EntityManagerRepository implements IUserRepository { 

	@Override
	public Optional<User> findUserByLogin(String login) {
		TypedQuery<User> selectUserQuery = 
				entityManager.createNamedQuery("User_FindByLogin", User.class);
		
		selectUserQuery.setParameter("paramLogin", login);
		try {
			return Optional.of(selectUserQuery.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
	
	@Override
	public User createUser(User user) {
		return executetransactionedMerge(user);
	}

	@Override
	public List<User> findAllUsers() {
		TypedQuery<User> selectUserQuery = 
				entityManager.createQuery("From User", User.class);
		return selectUserQuery.getResultList();
		
	}
	
} 
