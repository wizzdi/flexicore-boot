package com.flexicore.events;

import org.pf4j.PluginWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

import java.util.List;

public class PluginsLoadedEvent extends ApplicationContextEvent {
	private final List<PluginWrapper> startedPlugins;

	public PluginsLoadedEvent(ApplicationContext source, List<PluginWrapper> startedPlugins) {
		super(source);
		this.startedPlugins = startedPlugins;
	}

	public List<PluginWrapper> getStartedPlugins() {
		return startedPlugins;
	}


}
