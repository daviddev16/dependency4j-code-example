package com.example.core.swing;

import java.awt.Dimension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class LabeledDatePicker extends JPanel {

	private static final long serialVersionUID = 6077019931437551109L;
	
	public static final AbstractFormatter DATE_FORMATTER = new DateLabelFormatter();
	
	public static final Properties I18N_PROPERTIES = new Properties() 
	{
		private static final long serialVersionUID = 1L;
		{
			put("text.today", "Today");
			put("text.month", "Month");
			put("text.year", "Year");
		}
	};
	
	private final UtilDateModel   datePickerModel;
	private final JDatePickerImpl datePickerImpl;
	private final JDatePanelImpl  datePanel;
	private final JLabel lblTitle;

	public LabeledDatePicker() { this(""); }
	
	public LabeledDatePicker(String title) {
		
		setBorder(null);
	
		setPreferredSize(new Dimension(333, 63));
		setMaximumSize(new Dimension(333, 63));
		
		datePickerModel = new UtilDateModel();
		datePanel 		= new JDatePanelImpl(datePickerModel, I18N_PROPERTIES);
		datePickerImpl  = new JDatePickerImpl(datePanel, DATE_FORMATTER);
		
		lblTitle = new JLabel(title);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
				.addComponent(datePickerImpl, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblTitle)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(datePickerImpl, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(20))
		);

		setLayout(groupLayout);
	}
	
	public void setDate(LocalDate localDate) {
		
		datePickerModel.setDate(
				localDate.getYear(), 
				localDate.getMonthValue() - 1, 
				localDate.getDayOfMonth());

		datePickerModel.setSelected(true);
		datePickerImpl.repaint();
	}
	
	public LocalDate asLocalDate() {
		return LocalDate.of(datePickerModel.getYear(), 
							datePickerModel.getMonth() + 1, 
							datePickerModel.getDay());
	}

	public LocalDateTime asLocalDateTime(LocalTime localTime) {
		return LocalDateTime.of(asLocalDate(), localTime);
	}
	
	public void setTitle(String title) {
		lblTitle.setText(title);
	}

	public UtilDateModel getPickerModel() {
		return datePickerModel;
	}
	
}
