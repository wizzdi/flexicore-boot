package com.flexicore.init;

import com.wizzdi.flexicore.boot.base.events.PluginsLoadedEvent;
import com.wizzdi.flexicore.boot.base.init.EventPropagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ComaptibleEventPropegator {

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private static final AtomicBoolean init=new AtomicBoolean(false);

	@EventListener
	public void onEvent(PluginsLoadedEvent pluginsLoadedEvent){
		if(init.compareAndSet(false,true)){
			applicationEventPublisher.publishEvent(new com.flexicore.events.PluginsLoadedEvent(pluginsLoadedEvent.getApplicationContext(),pluginsLoadedEvent.getStartedPlugins()));
		}
	}
}
