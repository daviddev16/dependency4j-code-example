package com.example.config;

import io.github.dependency4j.Managed;

@Managed(disposable = false)
public final class AppSystemInfo {

	private final String APP_VERSION = "1.0";
	
	public String createFormattedModuleTitle(String moduleName) {
		return "%s - %s".formatted(moduleName, getApplicationName());
	}
	
	public String getApplicationName() {
		return "Dependency4J's Example Version " + APP_VERSION;
	}

}
