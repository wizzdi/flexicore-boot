package com.flexicore.init;

import com.wizzdi.flexicore.boot.base.events.PluginsLoadedEvent;
import com.wizzdi.flexicore.boot.base.init.EventPropagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ComaptibleEventPropegator {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	private EventPropagator eventPropagator;

	@EventListener
	public void onEvent(PluginsLoadedEvent pluginsLoadedEvent){
		eventPropagator.handleEvent(new com.flexicore.events.PluginsLoadedEvent(pluginsLoadedEvent.getApplicationContext(),pluginsLoadedEvent.getStartedPlugins()));
	}
}
