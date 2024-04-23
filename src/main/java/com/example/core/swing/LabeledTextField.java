package com.example.core.swing;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

public class LabeledTextField extends JPanel {

	private static final long serialVersionUID = 6077019931437551109L;
	
	public static final AbstractFormatter DATE_FORMATTER = new DateLabelFormatter();
	
	private final JLabel lblTitle;
	private JTextField txFdInput;
	
	public LabeledTextField() { this(""); }
	
	public LabeledTextField(String title) {
		
		setBorder(null);
	
		setPreferredSize(new Dimension(333, 49));
		setMaximumSize(new Dimension(333, 49));

		lblTitle = new JLabel(title);
		
		txFdInput = new JTextField();
		txFdInput.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
				.addComponent(txFdInput, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblTitle)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txFdInput, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
					.addGap(8))
		);

		setLayout(groupLayout);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		txFdInput.setEnabled(enabled);
		super.setEnabled(enabled);
	}
	
	public void setText(String text) {
		txFdInput.setText(text);
	}
	
	public String getText() {
		 return txFdInput.getText();
	}
}
