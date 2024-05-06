package com.wizzdi.flexicore.boot.base.init;


import com.wizzdi.flexicore.boot.base.events.PluginsLoadedEvent;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Configuration
@ComponentScan(basePackages = "com.wizzdi.flexicore.boot.base")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@AutoConfigureOrder(value =Integer.MIN_VALUE)
public class PluginInit  {

    private static final Logger logger = LoggerFactory.getLogger(PluginInit.class);
    public static final Comparator<PluginWrapper> PLUGIN_COMPARATOR = Comparator.comparing(f -> f.getDescriptor().getVersion());

    @Autowired
    @Lazy
    private PluginManager pluginManager;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private static final AtomicBoolean init=new AtomicBoolean(false);

    @EventListener
    public void onContextInit(ContextRefreshedEvent contextRefreshedEvent){
        if(contextRefreshedEvent.getApplicationContext().getId().equals(applicationContext.getId()) && init.compareAndSet(false,true)){
            List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins().stream().sorted(PLUGIN_COMPARATOR).collect(Collectors.toList());
            PluginsLoadedEvent event = new PluginsLoadedEvent(applicationContext, startedPlugins);
            eventPublisher.publishEvent(event);
        }
    }


}
