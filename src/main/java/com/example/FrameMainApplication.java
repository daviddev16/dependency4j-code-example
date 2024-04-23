package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.example.config.AppSystemInfo;
import com.example.core.DateUtil;
import com.example.core.FilterContainer;
import com.example.core.IconService;
import com.example.core.swing.CustomToolBarButton;
import com.example.core.swing.LabeledDatePicker;
import com.example.core.swing.LabeledTextField;
import com.example.core.swing.SwingUtil;
import com.example.inventory.InventoryFilterOptions;
import com.example.inventory.InventoryMovementType;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.service.InventoryService;
import com.example.inventory.ui.FrameInventoryManager;
import com.example.inventory.ui.InventoryResponseViewTable;
import com.example.product.ui.FrameProductManager;
import com.example.ui.UIDefaults;
import com.example.ui.UIMessages;
import com.example.user.service.UserService;

import io.github.dependency4j.DependencyManager;
import io.github.dependency4j.InstallationType;
import io.github.dependency4j.Managed;
import io.github.dependency4j.Pull;
import io.github.dependency4j.exception.ReflectionStateException;

@Managed(dynamic = true)
public class FrameMainApplication extends JFrame 
								  implements FilterContainer, ActionListener {

	public static final Border COMMON_LINE_BORDER = new LineBorder(Color.LIGHT_GRAY);
	
	private static final long serialVersionUID = 1L;

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface InternalEventHandler {
		
		ActionType value() default ActionType.ACTION_NONE;

	};
	
	public enum ActionType 
	{
		ACTION_MANAGE_PRODUCTS,
		ACTION_CLEAR_INVENTORY,
		ACTION_CLEAR_SELECTED_INVENTORY,
		ACTION_D4J_GITHUB,
		ACTION_NEW_IN_OUT,
		ACTION_PERFORM_FILTER,
		ACTION_RESET_FILTER,
		ACTION_NONE;
	};
	
	private final Map<ActionType, Consumer<ActionEvent>> EVENT_MAPPER = 
			new HashMap<FrameMainApplication.ActionType, Consumer<ActionEvent>>();

	private final InventoryService	inventoryService;
	private final DependencyManager	dependencyManager;
	private final UserService		userService;
	private final AppSystemInfo		appSystemInfo;
	private final UIMessages		uiMessages;
	private final IconService		iconService;
	
	private JToolBar mainToolBar;
	
	private JPanel contentPane;
	private JPanel pnFilter;
	private JPanel pnFooter;
	
	private JScrollPane scrPnInventory;

	private InventoryResponseViewTable tbInventoryView;
	
	private JComboBox<String> cmbBxType;
	
	private LabeledDatePicker lblEndDatePicker;
	private LabeledDatePicker lblStartDatePicker;
	private LabeledTextField txFdUserFilter;

	private JLabel lblStatus;
	private JLabel lblAuthenticatedAs;
	private JLabel lblType;
	
	private JButton btnResult;
	private JButton btnFilter;
	
	private CustomToolBarButton btnNewInOut;
	private CustomToolBarButton btnManageProducts;
	private CustomToolBarButton btnCancel;
	private CustomToolBarButton btnGithub;
	private Component horizontalGlue;
	private CustomToolBarButton btnClearInventory;

	@Pull
	public FrameMainApplication(InventoryService inventoryService,
								UserService userService, 
						  		AppSystemInfo appSystemInfo, 
						  		DependencyManager dependencyManager,
						  		UIMessages uiMessages,
						  		IconService iconService) 
	{
		this.inventoryService  = inventoryService;
		this.userService       = userService;
		this.appSystemInfo     = appSystemInfo;
		this.dependencyManager = dependencyManager;
		this.uiMessages        = uiMessages;
		this.iconService       = iconService;
		
		initializeComponents();
	}
	
	private void initializeComponents() 
	{
		mapAllMethodsToEventMapper();
		
		/* JFRAME INITIALIZATION */
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1172, 790);
		setMinimumSize(new Dimension(800, 600));
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setTitle(appSystemInfo.createFormattedModuleTitle("Main"));
		setContentPane(contentPane);
		
		/* MAIN INVENTORY TABLE & PANEL SETUP */
		
		{
			scrPnInventory = new JScrollPane();
			scrPnInventory.setBorder(new LineBorder(Color.LIGHT_GRAY));

			tbInventoryView = new InventoryResponseViewTable();

			/* initialize InventoryResponseViewTable with dependency injection */
			dependencyManager.installInstance(tbInventoryView, InstallationType.STANDALONE);

			tbInventoryView.setBorder(null);
			scrPnInventory.setViewportView(tbInventoryView);
		}

		/* FOOTER PANEL SETUP */
		
		{
			pnFooter = new JPanel();
			pnFooter.setBorder(COMMON_LINE_BORDER);

			lblStatus = new JLabel("");
			pnFooter.add(lblStatus);
		}
		
		/* FILTER PANEL SETUP */
		
		{
			pnFilter = new JPanel();
			pnFilter.setLayout(null);
			pnFilter.setBorder(COMMON_LINE_BORDER);

			JPanel pnFilterTitle = new JPanel();
			pnFilterTitle.setBackground(UIDefaults.ACCENT_COLOR);
			pnFilterTitle.setBounds(1, 1, 224, 37);
			pnFilter.add(pnFilterTitle);
			pnFilterTitle.setLayout(new BorderLayout(0, 0));

			JLabel lblFilterTitle = new JLabel("Filter");
			lblFilterTitle.setFont(lblFilterTitle.getFont().deriveFont(Font.BOLD));
			lblFilterTitle.setForeground(Color.WHITE);
			lblFilterTitle.setHorizontalAlignment(SwingConstants.CENTER);
			pnFilterTitle.add(lblFilterTitle);

			lblStartDatePicker = new LabeledDatePicker("Start Date:");
			lblStartDatePicker.setBounds(12, 46, 205, 45);
			pnFilter.add(lblStartDatePicker);

			lblEndDatePicker = new LabeledDatePicker("End Date:");
			lblEndDatePicker.setBounds(11, 95, 206, 47);
			pnFilter.add(lblEndDatePicker);

			cmbBxType = new JComboBox<String>();
			cmbBxType.setModel(SwingUtil.createInventoryTypeComboBoxModel(true));
			cmbBxType.setBounds(10, 170, 96, 25);
			pnFilter.add(cmbBxType);

			lblType = new JLabel("Type:");
			lblType.setBounds(10, 153, 46, 14);
			pnFilter.add(lblType);

			btnFilter = new JButton("Filter");
			SwingUtil.initializeButtonActionHandler(this, btnFilter, ActionType.ACTION_PERFORM_FILTER);
			getRootPane().setDefaultButton(btnFilter);
			btnFilter.setBounds(131, 263, 85, 25);
			pnFilter.add(btnFilter);

			txFdUserFilter = new LabeledTextField("User login:");
			txFdUserFilter.setBounds(12, 206, 205, 49);
			pnFilter.add(txFdUserFilter);

			btnResult = new JButton("Reset");
			SwingUtil.initializeButtonActionHandler(this, btnResult, ActionType.ACTION_RESET_FILTER);
			btnResult.setBounds(36, 264, 85, 25);
			pnFilter.add(btnResult);

			lblAuthenticatedAs = new JLabel("");
			lblAuthenticatedAs.setHorizontalAlignment(SwingConstants.TRAILING);
			lblAuthenticatedAs.setBounds(1, 306, 216, 19);
			pnFilter.add(lblAuthenticatedAs);
		}
		
		/* TOOLBAR AND BUTTONS SETUP */
		
		{
			mainToolBar = new JToolBar();
			mainToolBar.add(Box.createHorizontalStrut(5));

			btnManageProducts = new CustomToolBarButton("Manage Products");
			SwingUtil.initializeButtonActionHandler(this, btnManageProducts, ActionType.ACTION_MANAGE_PRODUCTS);
			btnManageProducts.setIcon(iconService.getNamedIcon("products"));
			mainToolBar.add(btnManageProducts);


			btnNewInOut = new CustomToolBarButton("New In / Out");
			SwingUtil.initializeButtonActionHandler(this, btnNewInOut, ActionType.ACTION_NEW_IN_OUT);
			btnNewInOut.setIcon(iconService.getNamedIcon("fill"));
			mainToolBar.add(btnNewInOut);

			btnCancel = new CustomToolBarButton("Clear Selected Inventory");
			SwingUtil.initializeButtonActionHandler(this, btnCancel, ActionType.ACTION_CLEAR_SELECTED_INVENTORY);
			btnCancel.setIcon(iconService.getNamedIcon("cancel"));
			mainToolBar.add(btnCancel);

			btnClearInventory = new CustomToolBarButton("Clear All Inventory");
			SwingUtil.initializeButtonActionHandler(this, btnClearInventory, ActionType.ACTION_CLEAR_INVENTORY);
			btnClearInventory.setIcon(iconService.getNamedIcon("clear"));
			mainToolBar.add(btnClearInventory);

			horizontalGlue = Box.createHorizontalGlue();
			mainToolBar.add(horizontalGlue);

			btnGithub = new CustomToolBarButton("Dependency4J Project");
			SwingUtil.initializeButtonActionHandler(this, btnGithub, ActionType.ACTION_D4J_GITHUB);
			btnGithub.setIcon(iconService.getNamedIcon("github"));
			mainToolBar.add(btnGithub);

			mainToolBar.add(Box.createHorizontalStrut(5));
		}

		/* DISPLAY AUTHENTICATED USER */
		
		lblAuthenticatedAs.setText("<html>Authenticated as <b>%s</b></html>"
				.formatted(userService.getAuthenticatedUser().getLogin()));
		
		/* LAYOUT SETUP */
		
		{
			GroupLayout gl_contentPane = new GroupLayout(contentPane);
			gl_contentPane.setHorizontalGroup(
					gl_contentPane.createParallelGroup(Alignment.TRAILING)
					.addComponent(mainToolBar, GroupLayout.DEFAULT_SIZE, 1156, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
									.addComponent(pnFooter, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 904, Short.MAX_VALUE)
									.addComponent(scrPnInventory, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 904, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(pnFilter, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
					);
			gl_contentPane.setVerticalGroup(
					gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(mainToolBar, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(scrPnInventory, GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(pnFooter, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
									.addComponent(pnFilter, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE))
							.addContainerGap())
					);

			contentPane.setLayout(gl_contentPane);
		}
		
		/* INITIALIZE */
		
		initializeFilterLayout();
	}
	
	/**
	 * maybe implements a better and generic way for event handling with reflected methods.
	 * This is just a hard coded event mapper function.
	 **/
	private void mapAllMethodsToEventMapper() {
		for (Method method : getClass().getDeclaredMethods()) {
			InternalEventHandler internalEventHandler = method.getAnnotation(InternalEventHandler.class);
			if (internalEventHandler != null)  {
				EVENT_MAPPER.put(internalEventHandler.value(), (actionEvent) -> 
					invokeMethodBasedOnActionEvent(method, actionEvent));
			}
		}
	}
	
	/**
	 * maybe implements a better and generic way for event handling with reflected methods.
	 * This is just a hard coded event mapper function.
	 **/
	private Object invokeMethodBasedOnActionEvent(Method method, ActionEvent actionEvent) {
		try {
			if (method.getParameterCount() == 0)
				return method.invoke(this, new Object[0]);

			else if (method.getParameterCount() == 1 && method.getParameters()[0].getType()
						.isAssignableFrom(ActionEvent.class)) {
					return method.invoke(this, actionEvent);
			}
			throw new IllegalStateException("Methods annotated with InternalEventHandler "
					+ "can't have more than 1 parameter.");
		} catch (Exception e) {
			throw new ReflectionStateException("Could not invoke \"" + method.getName() + "\".", e);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		try {
			ActionType actionType = ActionType.valueOf(actionEvent.getActionCommand());
			if (actionType == null)
				return;

			Consumer<ActionEvent> eventConsumer = EVENT_MAPPER.get(actionType);
			if (eventConsumer != null)
				eventConsumer.accept(actionEvent);

		} catch (Exception e) {
			uiMessages.displayError(e.getMessage(), "Error");
			e.printStackTrace();
		}
	}
	
	@InternalEventHandler(ActionType.ACTION_MANAGE_PRODUCTS)
	private void executeOpenProductManagerAction() {
		
		FrameProductManager frameProductManager = dependencyManager
				.installType(FrameProductManager.class, InstallationType.STANDALONE);

		SwingUtil.displayOnCenter(frameProductManager);

		/* process filter again when close FrameProductManager */
		processCurrentFilterLayout();
	}
	
	@InternalEventHandler(ActionType.ACTION_NEW_IN_OUT)
	private void executeOpenInventoryManagerAction() {
		
		FrameInventoryManager frameInventoryManager = dependencyManager
				.installType(FrameInventoryManager.class, InstallationType.STANDALONE);

		SwingUtil.displayOnCenter(frameInventoryManager);

		/* process filter again when close FrameInventoryManager */
		processCurrentFilterLayout();
	}
	
	@InternalEventHandler(ActionType.ACTION_PERFORM_FILTER)
	public void executePerformFilterAction() {
		processCurrentFilterLayout();
	}
	
	@InternalEventHandler(ActionType.ACTION_RESET_FILTER)
	public void executeResetFilterAction() {
		initializeFilterLayout();
	}
	
	@InternalEventHandler(ActionType.ACTION_CLEAR_INVENTORY)
	private void executeClearInventoryAction() {
		inventoryService.clearInventory();
		processCurrentFilterLayout();
	}
	
	@InternalEventHandler(ActionType.ACTION_CLEAR_SELECTED_INVENTORY)
	private void executeClearSelectedInventoryAction() {
		
		Long referencedInventoryId = tbInventoryView
				.getSelectedInventoryReferenceHolder().getInventoryId();
		
		if (referencedInventoryId != null)
			inventoryService.clearInventoryById(referencedInventoryId);
		
		processCurrentFilterLayout();
	}
	
	@InternalEventHandler(ActionType.ACTION_D4J_GITHUB)
	private void executeOpenDependency4JGithubWebPageAction() {
		try {
			Desktop.getDesktop().browse(new URI("https://github.com/daviddev16/dependency4j"));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void processCurrentFilterLayout() {
		
		lblStatus.setText("");
		InventoryFilterOptions filterOptions = createInventoryFilterOptions();

		List<InventoryResponse> inventoryResponseList = 
				inventoryService.fetchInventoriesFromFilterOptions(filterOptions);

		tbInventoryView.displayDataSet(inventoryResponseList);
		lblStatus.setText(inventoryService.createSummaryRichText(inventoryResponseList));
	}

	@Override
	public void initializeFilterLayout() {
		
		/* CLEANING SETTINGS */
		lblStartDatePicker.setDate(LocalDate.now().minusMonths(1L));
		lblEndDatePicker.setDate(LocalDate.now());
		cmbBxType.setSelectedIndex(0);
		txFdUserFilter.setText("");
		
		/* PROCESS FILTER */
		processCurrentFilterLayout();
	}
	
	private InventoryFilterOptions createInventoryFilterOptions() {
		return InventoryFilterOptions
				.builder()
					.endDateTime(DateUtil
							.asEndLocalDateTime(lblEndDatePicker.asLocalDate()))
					.startDateTime(DateUtil
							.asStartLocalDateTime(lblStartDatePicker.asLocalDate()))
					.inventoryMovementType(
							getFilterSelectedInventoryMovementType())
					.userLogin(txFdUserFilter.getText())
				.build();
	}
	
	private InventoryMovementType getFilterSelectedInventoryMovementType() {
		String selectedStringValue = (String) cmbBxType.getSelectedItem();
		return InventoryMovementType.getInventoryMovementTypeByAlias(selectedStringValue);
	}

} 
