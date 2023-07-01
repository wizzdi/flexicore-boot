/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.interfaces.Plugin;

public class PluginInformation<T extends Plugin> {
	private T plugin;
	private String jarLocation;

	public PluginInformation(T plugin,String jarLocation) {
		this.plugin=plugin;
		this.jarLocation=jarLocation;
	}

	public T getPlugin() {
		return plugin;
	}

	public void setPlugin(T plugin) {
		this.plugin = plugin;
	}

	public String getJarLocation() {
		return jarLocation;
	}

	public void setJarLocation(String jarLocation) {
		this.jarLocation = jarLocation;
	}
	
	

}
