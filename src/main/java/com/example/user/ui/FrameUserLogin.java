package com.example.user.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.example.FrameMainApplication;
import com.example.config.AppSystemInfo;
import com.example.core.exception.ServiceException;
import com.example.core.swing.SwingUtil;
import com.example.ui.UIMessages;
import com.example.user.service.UserService;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import io.github.dependency4j.DependencyManager;
import io.github.dependency4j.Managed;
import io.github.dependency4j.Pull;

@Managed(dynamic = true)
public class FrameUserLogin extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final String LOGIN_ACTION_COMMAND = "LOGIN";
	
	private final DependencyManager dependencyManager;
	
	private final UserService   userService;
	private final AppSystemInfo appSystemInfo;
	private final UIMessages    uiMessages;
	
	private JPanel     contentPane;
	private JTextField txFdLogin;
	private JTextField txFdPassword;
	private JButton    btnLogin;

	@Pull
	public FrameUserLogin(UserService userService, 
						  AppSystemInfo appSystemInfo, 
						  DependencyManager dependencyManager,
						  UIMessages uiMessages) 
	{
		this.userService       = userService;
		this.appSystemInfo     = appSystemInfo;
		this.dependencyManager = dependencyManager;
		this.uiMessages        = uiMessages;
		
		initializeComponents();
	}
	
	private void initializeComponents() {
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 146);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle(appSystemInfo.createFormattedModuleTitle("Login"));
		setContentPane(contentPane);
		
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("103px"),
				ColumnSpec.decode("46px"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("86px"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("86px"),},
			new RowSpec[] {
				FormSpecs.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblLogin = new JLabel("Login:");
		contentPane.add(lblLogin, "1, 2, left, center");
		
		txFdLogin = new JTextField();
		txFdLogin.setText("MANAGER");
		contentPane.add(txFdLogin, "2, 2, 5, 1, fill, top");
		txFdLogin.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		contentPane.add(lblPassword, "1, 4, left, default");
		
		txFdPassword = new JPasswordField();
		txFdPassword.setText("123");
		txFdPassword.setColumns(10);
		contentPane.add(txFdPassword, "2, 4, 5, 1, fill, top");
		
		btnLogin = new JButton("Login");
		btnLogin.setActionCommand(LOGIN_ACTION_COMMAND);
		btnLogin.addActionListener(this);
		contentPane.add(btnLogin, "6, 6");
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(LOGIN_ACTION_COMMAND)) 
			validateUserLogin(txFdLogin.getText(), txFdPassword.getText());
		
	}
	
	private void validateUserLogin(String login, String password) {
		try {
			
			if (userService.authenticateUser(login, password))
				closeAndOpenMainApplication();
			
		} catch (ServiceException e) {
			uiMessages.displayError(e.getMessage(), "Authentication failed");
		}
	}
	
	private void closeAndOpenMainApplication() {
		
		FrameMainApplication frameMainApplication = dependencyManager
				.installType(FrameMainApplication.class);
		
		SwingUtil.displayOnCenter(frameMainApplication);

		dispose();
	}

}
