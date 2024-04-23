package com.example.user.repository;

import java.util.Optional;

import com.example.user.User;

public interface IUserRepository {

	User createUser(User user);
	
	Optional<User> findUserByLogin(String login);
	
}
