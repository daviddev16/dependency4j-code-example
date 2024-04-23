package com.example.core;

public interface Paginator<T> {

	void update();
	
	T getCurrent();
	
	int getOffset();
	
	long getSize();
	
	void next();
	
	void previous();
	
}
