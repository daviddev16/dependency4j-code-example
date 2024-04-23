package com.example.ui;

import javax.swing.JOptionPane;

import io.github.dependency4j.Managed;

@Managed(disposable = false)
public final class UIMessages {

	public void displayError(String message, String title) {
		displayDialog(message, message, JOptionPane.ERROR_MESSAGE);
	}
	
	public void displayWarning(String message, String title) {
		displayDialog(message, message, JOptionPane.WARNING_MESSAGE);
	}
	
	public void displayInfo(String message, String title) {
		displayDialog(message, message, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void displayInfo(String message) {
		displayDialog(message, "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	private void displayDialog(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, null);
	}
	
}
