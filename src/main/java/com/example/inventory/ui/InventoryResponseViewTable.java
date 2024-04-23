package com.example.inventory.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.example.core.IconNameHolder;
import com.example.core.IconService;
import com.example.inventory.InventoryReferenceHolder;
import com.example.inventory.InventoryUtil;
import com.example.inventory.dto.InventoryResponse;
import com.example.ui.UIDefaults;

import io.github.dependency4j.Pull;

public class InventoryResponseViewTable extends JTable implements TableCellRenderer {

	public static final Border EMPTY_INSETS_5_BORDER = new EmptyBorder(5, 5, 5, 5);
	public static final Border CELL_BORDER = new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(4, 4, 4, 4));
	public static final Color C1 = new Color(240, 240, 240);

	private static final long serialVersionUID = 4670483956476116227L;

	final String[] columnIdentifiers = new String[] {
			"",
			"Product Name", 
			"Inventory Date", 
			"Type",
			"Unit Cost Price", 
			"Unit Sell Price", 
			"Amount", 
			"Total Stock",
			"User"};
	
	private DefaultTableModel tableModel;
	
	private @Pull IconService iconService;
	
	public InventoryResponseViewTable() { 
		super(); 
		
		setRowHeight(40);

		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 2322215168792577933L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		setColumnModel(new DefaultTableColumnModel());
		getTableHeader().setPreferredSize(new Dimension(0, 40));
		getTableHeader().setReorderingAllowed(false);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellSelectionEnabled(true);
		setRowSelectionAllowed(true);	
		setShowVerticalLines(false);
		
		tableModel.setColumnIdentifiers(columnIdentifiers);
		setDefaultRenderer(Object.class, this);
		setModel(tableModel);
		
		TableColumn iconColumn = getColumnModel().getColumn(0);
		iconColumn.setMinWidth(40);
		iconColumn.setMaxWidth(40);
	}
	
	private Vector<Object> convertInventoryResponseToVector(InventoryResponse inventoryResponse) {
		Vector<Object> vector = new Vector<Object>(columnIdentifiers.length);
		
		/* InventoryReferenceHolder helps to preserve reference to Inventory Id without rendering it on table */
		vector.add(new InventoryReferenceHolder(
				inventoryResponse.getInventoryMovementType().getIconNameHolder(), 
				inventoryResponse.getInventoryId()));
		
		vector.add(inventoryResponse.getProductName());
	
		vector.add(inventoryResponse.getInventoryDateTime()
				.format(UIDefaults.COMMON_DATE_FORMATTER));
		
		vector.add(inventoryResponse.getInventoryMovementType()
				.getAlias());
		
		vector.add(inventoryResponse.getUnitCostPrice());
		vector.add(inventoryResponse.getUnitSellPrice());
		
		vector.add(InventoryUtil.negateWhenOutput(inventoryResponse.getAmount(), 
				inventoryResponse.getInventoryMovementType()));
		
		vector.add(InventoryUtil.negateWhenOutput(inventoryResponse.getTotal(), 
				inventoryResponse.getInventoryMovementType()));
		
		vector.add(inventoryResponse.getUserName());
		
		return vector;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		JLabel componentLabel = new JLabel();
		componentLabel.setOpaque(isSelected);

		if (value instanceof IconNameHolder iconNameHolder)
			componentLabel.setIcon(iconService.getNamedIcon(iconNameHolder.getIconName()));

		else if (value instanceof BigDecimal bigDecimal)
			componentLabel.setText(UIDefaults.MONETARY_FORMAT.format(bigDecimal));
		else
			componentLabel.setText(value.toString());	

		componentLabel.setBorder(UIDefaults.EMPTY_BORDER_INSETS_5);

		if (isSelected)
			componentLabel.setForeground(Color.white);
		else
			componentLabel.setForeground(Color.black);

		componentLabel.setBackground(UIDefaults.ACCENT_COLOR);
		componentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		componentLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		
		return componentLabel;
	}
	
	public void displayDataSet(List<InventoryResponse> inventoryResponseList) {
		tableModel.setRowCount(0);

		inventoryResponseList.stream()
			.map(this::convertInventoryResponseToVector)
			.forEach(irVector -> tableModel.addRow(irVector));
		
		setAutoCreateRowSorter(true);
		TableRowSorter<?> rowSorter = (TableRowSorter<?>) getRowSorter();
		rowSorter.setSortsOnUpdates(true);
		revalidate();
	}
	
	public InventoryReferenceHolder getInventoryReferenceHolderFromRow(int row) {
		return (InventoryReferenceHolder) getValueAt(row, 0);
	}

	public InventoryReferenceHolder getSelectedInventoryReferenceHolder() {
		return getInventoryReferenceHolderFromRow(getSelectedRow());
	}
}
