package com.example.core.swing;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class CustomToolBarButton extends JButton {

	private static final long serialVersionUID = -4044567104681754398L;

	public CustomToolBarButton(String text) {
		super(text);
		setBorder(new EmptyBorder(7, 7, 7, 7));
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setVerticalAlignment(SwingConstants.TOP);
	}
	
}
