package com.example.user.service;

import com.example.core.Encoder;
import com.example.core.exception.ServiceException;
import com.example.user.User;
import com.example.user.repository.IUserRepository;

import io.github.dependency4j.Managed;
import io.github.dependency4j.Pull;

@Managed(disposable = false)
public class UserService {

	private User authenticatedUser;

	private final IUserRepository userRepository;
	private final Encoder passwordEncoder;
	
	public @Pull UserService(Encoder passwordEncoder, 
					   		 IUserRepository userRepository) 
	{
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
	}
	
	public synchronized boolean authenticateUser(String login, String password) {
		
		User loggedUser = findUserByLogin(login);
		
		String encodedPassword = loggedUser.getPassword();
		
		if (!passwordEncoder.match(password, encodedPassword))
			throw new ServiceException("Password does not match.");
		
		authenticatedUser = loggedUser;

		return true;
	}
	
	public User findUserByLogin(String login) {
		return userRepository
				.findUserByLogin(login)
				.orElseThrow(() -> new ServiceException("\"%s\" user could not be found.".formatted(login)));
	}
	
	public User createUser(String login, String password) {
		password = passwordEncoder.encode(password);
		
		User newUser = userRepository.createUser(
				User.builder()
						.login(login)
						.password(password)
					.build());
		
		return newUser;
	}
	
	public User getAuthenticatedUser() {
		return authenticatedUser;
	}

}
