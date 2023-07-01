package com.wizzdi.flexicore.boot.base.init;

import org.pf4j.PluginWrapper;
import org.pf4j.spring.ExtensionsInjector;
import org.pf4j.spring.SpringPluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;

import java.util.List;
import java.util.Set;

public class FlexiCoreExtensionsInjector extends ExtensionsInjector {

    private static final Logger logger=LoggerFactory.getLogger(FlexiCoreExtensionsInjector.class);

    public FlexiCoreExtensionsInjector(SpringPluginManager pluginManager, AbstractAutowireCapableBeanFactory beanFactory) {
        super(pluginManager, beanFactory);
    }

    @Override
    protected void registerExtension(Class<?> extensionClass) {
        Object extension = this.springPluginManager.getExtensionFactory().create(extensionClass);

    }


    @Override
    public void injectExtensions() {
        // add extensions for each started plugin
        List<PluginWrapper> startedPlugins = springPluginManager.getStartedPlugins();
        for (PluginWrapper plugin : startedPlugins) {
            logger.debug("Registering extensions of the plugin '{}' as beans", plugin.getPluginId());
            Set<String> extensionClassNames = springPluginManager.getExtensionClassNames(plugin.getPluginId());
            for (String extensionClassName : extensionClassNames) {
                try {
                    logger.debug("Register extension '{}' as bean", extensionClassName);
                    Class<?> extensionClass = plugin.getPluginClassLoader().loadClass(extensionClassName);
                    registerExtension(extensionClass);
                } catch (ClassNotFoundException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
