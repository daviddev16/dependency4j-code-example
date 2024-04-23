package com.example.core.exception;

public class NotFoundException extends ServiceException {

	private static final long serialVersionUID = 1338578197784905207L;

	public NotFoundException(String message) {
		super(message);
	}

}
