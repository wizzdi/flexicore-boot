package com.wizzdi.flexicore.boot.base.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.EventObject;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * will propagate events from one application contexts to all leaf application contexts ( only leafs are required as application context propagate the events to their parents by default)
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EventPropagator {

    @Autowired
    @Lazy
    private FlexiCorePluginManager flexiCorePluginManager;
    private static final Set<Object> eventsInProcess = new ConcurrentSkipListSet<>(Comparator.comparing(System::identityHashCode));
    private static final Logger logger = LoggerFactory.getLogger(EventPropagator.class);


    /**
     * the eventsInProcess set is required to prevent infinite recursion
     *
     * @param event event to send to all plugin contexts
     */
    @EventListener
    public void handleEvent(EventObject event) {
        if (eventsInProcess.add(event)) {
            Object eventToPrint = event instanceof PayloadApplicationEvent ? ((PayloadApplicationEvent<?>) event).getPayload() : event;

            logger.debug("Propagating event " + eventToPrint);
            try {
                for (ApplicationContext applicationContext : flexiCorePluginManager.getPluginApplicationContexts()) {
                    long start = System.currentTimeMillis();
                    try {

                        if (event.getSource() != applicationContext) {
                            Object contextId = applicationContext.getClassLoader() instanceof FlexiCorePluginClassLoader ? applicationContext.getClassLoader() : applicationContext.getId();
                            logger.debug("Propagating event " + eventToPrint + " to context " + contextId);
                            if (applicationContext instanceof FlexiCoreApplicationContext) {
                                ((FlexiCoreApplicationContext) applicationContext).publishEventNoParent(event);
                            } else {
                                applicationContext.publishEvent(event);
                            }
                            logger.debug("Propagating event " + eventToPrint + " to context " + contextId + " took " + (System.currentTimeMillis() - start) + "ms");

                        }
                    } catch (Exception e) {
                        logger.error("error while propagating event: " + eventToPrint, e);
                    }
                }
            } finally {
                eventsInProcess.remove(event);
            }
        }

    }
}
