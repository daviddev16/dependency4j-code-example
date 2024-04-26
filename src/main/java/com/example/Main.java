package com.example;

import java.awt.Toolkit;
import java.io.File;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

import com.example.core.swing.SwingUtil;
import com.example.user.ui.FrameUserLogin;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import io.github.dependency4j.DependencyManager;
import io.github.dependency4j.InstallationType;

public class Main {

	public static void main(String[] args) {

		CountDownLatch latch = new CountDownLatch(1);

		/* setup dependency manager installation */
		final DependencyManager dependencyManager = DependencyManager
				.builder()
				.installPackage("com.example")
				.getDependencyManager();
		
		/* create data in database */
		dependencyManager
			.query(EntityBasicSetup.class)
			.setupBasicEntities();
		
		/* self installation enables to inject DependencyManager on Managed classes  */
		dependencyManager.includeDependencyManagerAsDependency();
		
		/* release application frame thread */
		latch.countDown();
		
		SwingUtilities.invokeLater(() -> 
		{
			try {
				latch.await();

				/* setup application theme */				
				FlatLaf.registerCustomDefaultsSource(new File("./conf/theme"));
				Toolkit.getDefaultToolkit().setDynamicLayout(true);
				FlatLightLaf.setup();
				
				/* installing the user login frame */
				FrameUserLogin frameUserLogin = dependencyManager
						.installType(FrameUserLogin.class, InstallationType.STANDALONE);
				
				SwingUtil.displayOnCenter(frameUserLogin);
				frameUserLogin = null;
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}
}
