package com.example.core;

public interface Encoder {

	String encode(String text);
	
	boolean match(String text, String encodedText);
	
}
