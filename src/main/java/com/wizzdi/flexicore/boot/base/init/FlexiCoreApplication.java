package com.wizzdi.flexicore.boot.base.init;

import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ResourceLoader;

public class FlexiCoreApplication extends SpringApplication {

    public FlexiCoreApplication(Class<?>... primarySources) {
        super(primarySources);
    }

    public FlexiCoreApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
        super(resourceLoader, primarySources);
        init();
    }

    protected void init(){
        setApplicationContextFactory(webApplicationType -> {
            FlexiCoreAppBeanFactory flexiCoreAppBeanFactory = new FlexiCoreAppBeanFactory();
            FlexiCoreAppContext flexiCoreAppContext = new FlexiCoreAppContext(flexiCoreAppBeanFactory);
            flexiCoreAppBeanFactory.setApplicationContext(flexiCoreAppContext);
            return flexiCoreAppContext;
        });
    }
}
