package com.example.core.swing;

import java.awt.Window;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JSpinner;

import com.example.FrameMainApplication.ActionType;
import com.example.inventory.InventoryMovementType;

import io.github.dependency4j.util.Checks;

public final class SwingUtil {
	
	public static void displayOnCenter(Window frame) {
		Checks.nonNull(frame, "frame must not be null.");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void initializeButtonActionHandler(ActionListener actionListener, JButton handledButton, ActionType actionType) {
		if (actionListener != null && handledButton != null && actionType != null) {
			handledButton.addActionListener(actionListener);
			handledButton.setActionCommand(actionType.name());
			return;
		} 
		throw new IllegalStateException("Unabled to initalize handled JButton.");
	}
	
	public static DefaultComboBoxModel<String> createInventoryTypeComboBoxModel(boolean enableBoth) {
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
		if (enableBoth) {
			comboBoxModel.addElement("BOTH");
		}
		comboBoxModel.addElement(InventoryMovementType.INPUT.getAlias());
		comboBoxModel.addElement(InventoryMovementType.OUTPUT.getAlias());
		return comboBoxModel;
	}
	
	public static void commitChangesSafelyOf(JSpinner spinner) {
		try {
			spinner.commitEdit();
		} catch (ParseException e) {}
	}
	
}
