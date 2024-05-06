package com.wizzdi.flexicore.boot.base.init;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class FlexiCoreApplicationContext extends AnnotationConfigApplicationContext {
    private final FlexiCorePluginBeanFactory flexiCorePluginBeanFactory;
    private ApplicationEventMulticaster fcApplicationEventMulticaster;

    public FlexiCoreApplicationContext() {
        this(new FlexiCorePluginBeanFactory());
    }

    public FlexiCoreApplicationContext(FlexiCorePluginBeanFactory beanFactory) {
        super(beanFactory);
        this.flexiCorePluginBeanFactory = beanFactory;
    }


    @Override
    public FlexiCorePluginBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return flexiCorePluginBeanFactory;

    }

    @Override
    protected void initApplicationEventMulticaster() {
        super.initApplicationEventMulticaster();
    }

    protected void publishEventNoParent(Object event) {
        publishEventNoParent(event, null);
    }

    protected void publishEventNoParent(Object event, @Nullable ResolvableType eventType) {
        if(flexiCorePluginBeanFactory.isConfigurationFrozen()){
            if(fcApplicationEventMulticaster==null){
                fcApplicationEventMulticaster = getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
            }

            Assert.notNull(event, "Event must not be null");

            // Decorate event as an ApplicationEvent if necessary
            ApplicationEvent applicationEvent;
            if (event instanceof ApplicationEvent) {
                applicationEvent = (ApplicationEvent) event;
            } else {
                applicationEvent = new PayloadApplicationEvent<>(this, event);
                if (eventType == null) {
                    eventType = ((PayloadApplicationEvent<?>) applicationEvent).getResolvableType();
                }
            }

            fcApplicationEventMulticaster.multicastEvent(applicationEvent, eventType);
        }
        else{
            this.publishEvent(event,eventType);
        }

    }

    @Override
    protected void publishEvent(Object event, ResolvableType eventType) {
        ApplicationEvent applicationEvent;
        if (event instanceof ApplicationEvent) {
            applicationEvent = (ApplicationEvent) event;
        } else {
            applicationEvent = new PayloadApplicationEvent(this, event);
            if (eventType == null) {
                eventType = ((PayloadApplicationEvent) applicationEvent).getResolvableType();
            }
        }
        super.publishEvent(applicationEvent, eventType);
    }
}
