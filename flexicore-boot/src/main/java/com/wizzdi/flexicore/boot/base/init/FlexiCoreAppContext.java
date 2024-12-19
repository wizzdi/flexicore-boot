package com.wizzdi.flexicore.boot.base.init;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

import java.util.Queue;

public class FlexiCoreAppContext extends AnnotationConfigServletWebServerApplicationContext {
    private FlexiCoreAppBeanFactory flexiCoreAppBeanFactory;


    public FlexiCoreAppContext(FlexiCoreAppBeanFactory beanFactory) {
        super(beanFactory);
        this.flexiCoreAppBeanFactory=beanFactory;
    }

    public void updateContexts(Queue<FlexiCoreApplicationContext> pluginsApplicationContexts) {
       flexiCoreAppBeanFactory.updateContext(pluginsApplicationContexts);
    }
}
