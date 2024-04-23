package com.example.inventory.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;

import com.example.core.IconService;
import com.example.core.swing.CustomToolBarButton;
import com.example.core.swing.LabeledTextField;
import com.example.core.swing.SwingUtil;
import com.example.inventory.Inventory;
import com.example.inventory.InventoryMovementType;
import com.example.inventory.service.InventoryService;
import com.example.product.Product;
import com.example.product.service.ProductService;
import com.example.ui.UIMessages;
import com.example.user.User;
import com.example.user.service.UserService;

import io.github.dependency4j.Managed;
import io.github.dependency4j.Pull;

@Managed(dynamic = true)
public class FrameInventoryManager extends JDialog implements ActionListener  {

	private enum ActionType
	{
		ACTION_ADD_PRODUCT
	};
	
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;

	private final UserService      userService;
	private final ProductService   productService;
	private final InventoryService inventoryService;
	private final UIMessages 	   uiMessages;
	private final IconService      iconService;
	
	private LabeledTextField lblTxFdProductName;
	private JSpinner spnAmount;
	
	private CustomToolBarButton btnAdd;
	private JComboBox<String> cmbBxType;

	@Pull
	public FrameInventoryManager(ProductService productService, 
							     InventoryService inventoryService,
							     UIMessages uiMessages,
							     UserService userService,
							     IconService iconService) 
	{
		this.productService    = productService;
		this.inventoryService  = inventoryService;
		this.uiMessages        = uiMessages;
		this.userService       = userService;
		this.iconService       = iconService;

		initializeComponents();
	} 

	private void initializeComponents() {

		setTitle("Add Inventory Information");
		setMinimumSize(new Dimension(500, 300));
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		
		lblTxFdProductName = new LabeledTextField("Product Name: ");
		
		JToolBar toolBar = new JToolBar();
		
		btnAdd = new CustomToolBarButton("Add");
		btnAdd.setIcon(iconService.getNamedIcon("add"));
		btnAdd.setActionCommand(ActionType.ACTION_ADD_PRODUCT.name());
		btnAdd.addActionListener(this);
		
		toolBar.add(Box.createHorizontalGlue());

		toolBar.add(btnAdd);
		
		spnAmount = new JSpinner();
		spnAmount.setModel(new SpinnerNumberModel(Double.valueOf(0), null, null, Double.valueOf(1)));

		cmbBxType = new JComboBox<String>();
		cmbBxType.setModel(SwingUtil.createInventoryTypeComboBoxModel(false));
		
		JLabel lblAmount = new JLabel("Amount:");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(cmbBxType, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 188, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblAmount)
								.addComponent(spnAmount, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(13)
							.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblTxFdProductName, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(18)
					.addComponent(lblTxFdProductName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblAmount)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(spnAmount, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(cmbBxType, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!e.getActionCommand().equals(ActionType.ACTION_ADD_PRODUCT.name()))
			return;

		SwingUtil.commitChangesSafelyOf(spnAmount);
		
		final Product selectedProduct = getSelectedProduct();
		final User authenticatedUser = userService.getAuthenticatedUser();

		if (selectedProduct == null) {
			uiMessages.displayError("Product \"" + lblTxFdProductName.getText() + "\" not found.", "Error");
			return;
		}

		InventoryMovementType movementType = getSelectedInventoryMovementType();

		Inventory newUserInventory = Inventory
				.builder()
					.amount((Double)spnAmount.getValue())
					.inventoryDateTime(LocalDateTime.now())
					.product(selectedProduct)
					.user(authenticatedUser)
					.inventoryMovementType(movementType)
				.build();

		newUserInventory = inventoryService.createInventoryMovement(newUserInventory);

		uiMessages.displayInfo("Inventory with id \"" + newUserInventory.getId() + "\" was created!");
		
		dispose();
	}
	
	public Product getSelectedProduct() {
		String selectedName = lblTxFdProductName.getText();
		List<Product> products = productService.findProductsByName(selectedName);
		
		if (!products.isEmpty())
			return products.get(0);
		
		return null;
	}
	
	private InventoryMovementType getSelectedInventoryMovementType() {
		String selectedStringValue = (String) cmbBxType.getSelectedItem();
		return InventoryMovementType.getInventoryMovementTypeByAlias(selectedStringValue);
	}
}
