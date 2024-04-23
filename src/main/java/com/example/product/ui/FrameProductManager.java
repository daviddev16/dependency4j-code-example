package com.example.product.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;	
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.example.core.IconService;
import com.example.core.swing.CustomToolBarButton;
import com.example.core.swing.LabeledTextField;
import com.example.core.swing.SwingUtil;
import com.example.product.Product;
import com.example.product.ProductPaginator;
import com.example.product.service.ProductService;

import io.github.dependency4j.DependencyManager;
import io.github.dependency4j.InstallationType;
import io.github.dependency4j.Managed;
import io.github.dependency4j.Pull;
import javax.swing.SpinnerNumberModel;
import javax.swing.JLabel;

@Managed(dynamic = true)
public class FrameProductManager extends JDialog implements ActionListener  {

	private enum ActionType
	{
		ACTION_ADD_PRODUCT,
		ACTION_NEXT_PRODUCT,
		ACTION_PREVIOUS_PRODUCT,
		ACTION_EDIT_PRODUCT,		
		ACTION_FINISH_PRODUCT,
	};
	
	private enum EditMode {
		
		VIEW, EDIT, ADD
		
	};
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private final DependencyManager dependencyManager;
	private final ProductService productService;
	private final IconService iconService;
	
	private final ProductPaginator productPaginator;
	private LabeledTextField lblTxFdName;
	private JSpinner spnSellPrice;
	private JSpinner spnCostPrice;
	
	private TitledBorder titledBorder;
	private JPanel panel;
	
	private Product currentProduct;
	private EditMode editMode = EditMode.EDIT;
	private CustomToolBarButton btnEditOrFinish;
	private CustomToolBarButton btnBack;
	private CustomToolBarButton btnNext;
	private CustomToolBarButton btnAdd;

	@Pull
	public FrameProductManager(DependencyManager dependencyManager,
							   ProductService productService, 
							   IconService iconService) 
	{
		this.dependencyManager = dependencyManager;
		this.productService    = productService;
		this.iconService       = iconService;

		productPaginator =	this.dependencyManager
				.installType(ProductPaginator.class, InstallationType.STANDALONE);
		
		initializeComponents();
	} 

	private void initializeComponents() {

		setTitle("Product Manager");
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 479, 501);
		setMinimumSize(new Dimension(479, 501));
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		
		panel = new JPanel();
		panel.setEnabled(false);
		panel.setBorder(titledBorder = new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "", TitledBorder.TRAILING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		lblTxFdName = new LabeledTextField("Name: ");
		
		spnCostPrice = new JSpinner();
		spnCostPrice.setModel(new SpinnerNumberModel(Double.valueOf(0), null, null, Double.valueOf(1)));
		
		spnSellPrice = new JSpinner();
		spnSellPrice.setModel(new SpinnerNumberModel(Double.valueOf(0), null, null, Double.valueOf(1)));
		
		JLabel lblNewLabel = new JLabel("Sell Price:");
		
		JLabel lblCostPrice = new JLabel("Cost Price:");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblTxFdName, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(spnSellPrice, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblCostPrice)
								.addComponent(spnCostPrice, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTxFdName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(lblCostPrice))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(spnSellPrice, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(spnCostPrice, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(240, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		btnAdd = new CustomToolBarButton("Add");
		btnAdd.setIcon(iconService.getNamedIcon("add"));
		btnAdd.setActionCommand(ActionType.ACTION_ADD_PRODUCT.name());
		btnAdd.addActionListener(this);
		toolBar.add(btnAdd);
		
		Component horizontalStrut = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut);
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(2, 60));
		separator.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		toolBar.add(horizontalStrut_1);
		
		btnBack = new CustomToolBarButton("Back");
		btnBack.setIcon(iconService.getNamedIcon("back"));
		btnBack.setActionCommand(ActionType.ACTION_PREVIOUS_PRODUCT.name());
		btnBack.addActionListener(this);
		toolBar.add(btnBack);
		
		btnEditOrFinish = new CustomToolBarButton("Edit");
		btnEditOrFinish.setIcon(iconService.getNamedIcon("edit"));
		btnEditOrFinish.setActionCommand(ActionType.ACTION_EDIT_PRODUCT.name());
		btnEditOrFinish.addActionListener(this);
		toolBar.add(btnEditOrFinish);
		
		btnNext = new CustomToolBarButton("Next");
		btnNext.setIcon(iconService.getNamedIcon("next"));
		btnNext.setActionCommand(ActionType.ACTION_NEXT_PRODUCT.name());
		btnNext.addActionListener(this);
		toolBar.add(btnNext);
		contentPane.setLayout(gl_contentPane);
		
		setProductToView(currentProduct = productPaginator.getCurrent());
		changeStateOfAllComponentsOf(panel, false);
	}
	
	public Product createNewProductWithUserDefinitions() {
		return Product
				.builder()
					.name(lblTxFdName.getText())
					.costPrice(new BigDecimal((Double)spnCostPrice.getValue()))
					.sellPrice(new BigDecimal((Double)spnSellPrice.getValue()))
				.build();
	}

	public void updateCurrentProductEntityWithUserChanges() {
		
		if (!currentProduct.getName().equals(lblTxFdName.getText()))
			currentProduct.setName(lblTxFdName.getText());
		
		currentProduct.setCostPrice(new BigDecimal((Double)spnCostPrice.getValue()));
		currentProduct.setSellPrice(new BigDecimal((Double)spnSellPrice.getValue()));
		currentProduct = productService.updateProduct(currentProduct);
	}
	
	public void changeStateOfAllComponentsOf(JPanel panel, boolean state) {
		panel.setEnabled(state);
		for (Component component : panel.getComponents())
			component.setEnabled(state);
	}

	public void changeStateOfPageAndAddButtons(boolean state) {
		btnNext.setEnabled(state);
		btnBack.setEnabled(state);
		btnAdd.setEnabled(state);
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {

		ActionType actionType = ActionType.valueOf(actionEvent.getActionCommand());
		if (actionType == null)
			return;
		
		if (actionType == ActionType.ACTION_NEXT_PRODUCT) {

			productPaginator.next();
			retrieveAndDisplayCurrentProduct();
		}
		else if (actionType == ActionType.ACTION_PREVIOUS_PRODUCT) {
			
			productPaginator.previous();
			retrieveAndDisplayCurrentProduct();
		}
		else if (actionType == ActionType.ACTION_ADD_PRODUCT) {
			
			currentProduct = null;
			
			editMode = EditMode.ADD;
			btnEditOrFinish.setActionCommand(ActionType.ACTION_FINISH_PRODUCT.name());
			btnEditOrFinish.setIcon(iconService.getNamedIcon("finish"));
			btnEditOrFinish.setText("Register");

			updateTitleBorderValue("Creating new Product");
			
			changeStateOfAllComponentsOf(panel, true);
			changeStateOfPageAndAddButtons(false);
		}
		else if (actionType == ActionType.ACTION_EDIT_PRODUCT) {
			
			editMode = EditMode.EDIT;
			btnEditOrFinish.setActionCommand(ActionType.ACTION_FINISH_PRODUCT.name());
			btnEditOrFinish.setIcon(iconService.getNamedIcon("finish"));
			btnEditOrFinish.setText("Finish");
			
			updateTitleBorderValue("Editing " + currentProduct.getName());
			
			changeStateOfAllComponentsOf(panel, true);
			changeStateOfPageAndAddButtons(false);
		}
		else if (actionType == ActionType.ACTION_FINISH_PRODUCT)
		{
			SwingUtil.commitChangesSafelyOf(spnCostPrice);
			SwingUtil.commitChangesSafelyOf(spnSellPrice);
			
			if (editMode == EditMode.ADD)
			{
				Product newProduct = createNewProductWithUserDefinitions();
				currentProduct = productService.createProduct(newProduct);
				productPaginator.update();
			} 
			else 
			{
				updateCurrentProductEntityWithUserChanges();
			}
			
			editMode = EditMode.VIEW;
			btnEditOrFinish.setActionCommand(ActionType.ACTION_EDIT_PRODUCT.name());
			btnEditOrFinish.setIcon(iconService.getNamedIcon("edit"));
			btnEditOrFinish.setText("Edit");
			
			setProductToView(currentProduct);
			
			changeStateOfAllComponentsOf(panel, false);
			changeStateOfPageAndAddButtons(true);
			
			currentProduct = null;
		}
	}
	
	private void retrieveAndDisplayCurrentProduct() {
		currentProduct = productPaginator.getCurrent();
		setProductToView(currentProduct);
	}
	
	private void setProductToView(Product product) {	
		
		updateTitleBorderValue("Displaying " + product.getName());
		lblTxFdName .setText(product.getName());
		
		spnCostPrice.setValue(product.getCostPrice().doubleValue());
		spnSellPrice.setValue(product.getSellPrice().doubleValue());
	}
	
	public void updateTitleBorderValue(String newTitleText) {
		titledBorder.setTitle(newTitleText);
		panel.repaint();
	}
}
