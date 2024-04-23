package com.example.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.example.core.exception.ServiceException;

import io.github.dependency4j.Managed;

@Managed(disposable = false)
public class IconService {

	private final Map<String, ImageIcon> iconsMap;
	
	public IconService() {
		iconsMap = new HashMap<String, ImageIcon>();
		collectAllIcons(new File("./conf/img"));
	}
	
	private void collectAllIcons(File folder) {
		if (folder.isFile())
			throw new ServiceException("\"%s\" is not a folder."
					.formatted(folder.getAbsolutePath()));
		
		for (File child : folder.listFiles()) {
			
			if (child.isDirectory()) {
				collectAllIcons(child);
				continue;
			}
			
			String childFileName = child.getName();
			
			if (childFileName.endsWith(".png"))
				iconsMap.put(clearExtension(childFileName), 
						new ImageIcon(child.getAbsolutePath()));
		}
	}

	public ImageIcon getNamedIcon(String iconName) {
		return iconsMap.get(iconName);
	}
	
	private String clearExtension(String filename) {
		return filename.substring(0, filename.lastIndexOf('.'));
	}
	
}
