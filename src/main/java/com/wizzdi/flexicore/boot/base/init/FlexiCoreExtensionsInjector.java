package com.wizzdi.flexicore.boot.base.init;

import org.pf4j.spring.ExtensionsInjector;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;

public class FlexiCoreExtensionsInjector extends ExtensionsInjector {

    public FlexiCoreExtensionsInjector(SpringPluginManager pluginManager, AbstractAutowireCapableBeanFactory beanFactory) {
        super(pluginManager, beanFactory);
    }

    @Override
    protected void registerExtension(Class<?> extensionClass) {
        Object extension = this.springPluginManager.getExtensionFactory().create(extensionClass);

    }
}
