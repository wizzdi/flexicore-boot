package com.wizzdi.flexicore.boot.base.init;


import com.wizzdi.flexicore.boot.base.events.PluginsLoadedEvent;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.lang.invoke.VarHandle;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Configuration
@ComponentScan(basePackages = "com.wizzdi.flexicore.boot.base")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PluginInit implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(PluginInit.class);
    public static final Comparator<PluginWrapper> PLUGIN_COMPARATOR = Comparator.comparing(f -> f.getDescriptor().getVersion());

    @Autowired
    private PluginManager pluginManager;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    private static final AtomicBoolean init=new AtomicBoolean(false);


    /**
     * Starts the plug-in loader, uses a constant for its path
     * @return PluginsLoadedEvent
     */

    public void initPluginLoader() {
        if(init.compareAndSet(false,true)){
            logger.info("started FlexiCore plugin loader");
            List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins().stream().sorted(PLUGIN_COMPARATOR).collect(Collectors.toList());
            PluginsLoadedEvent event = new PluginsLoadedEvent(applicationContext, startedPlugins);
            eventPublisher.publishEvent(event);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initPluginLoader();
    }
}
