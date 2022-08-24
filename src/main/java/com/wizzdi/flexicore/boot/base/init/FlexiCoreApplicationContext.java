package com.wizzdi.flexicore.boot.base.init;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;

public class FlexiCoreApplicationContext extends AnnotationConfigApplicationContext {
    private final FlexiCorePluginBeanFactory flexiCorePluginBeanFactory;

    public FlexiCoreApplicationContext() {
       this(new FlexiCorePluginBeanFactory());
    }

    public FlexiCoreApplicationContext(FlexiCorePluginBeanFactory beanFactory) {
        super(beanFactory);
        this.flexiCorePluginBeanFactory =beanFactory;
    }


    @Override
    public FlexiCorePluginBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return flexiCorePluginBeanFactory;

    }

    @Override
    protected void publishEvent(Object event, ResolvableType eventType) {
        ApplicationEvent applicationEvent;
        if (event instanceof ApplicationEvent) {
            applicationEvent = (ApplicationEvent)event;
        } else {
            applicationEvent = new PayloadApplicationEvent(this, event);
            if (eventType == null) {
                eventType = ((PayloadApplicationEvent)applicationEvent).getResolvableType();
            }
        }
        super.publishEvent(applicationEvent,eventType);
    }
}
