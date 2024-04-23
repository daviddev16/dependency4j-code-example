package com.example.core;

public interface FilterContainer {

	void processCurrentFilterLayout();
	
	void initializeFilterLayout();
	
	default void clearAndProcessFilterLayout() 
	{
		initializeFilterLayout();
		processCurrentFilterLayout();
	}
	
}
