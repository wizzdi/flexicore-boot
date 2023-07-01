package com.flexicore.plugin;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.List;

public class FlexiCorePlugin extends Plugin {
    /**
     * Constructor to be used by plugin manager for plugin instantiation.
     * Your plugins have to provide constructor with this exact signature to
     * be successfully loaded by manager.
     *
     * @param wrapper
     */
    public FlexiCorePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    /**
     * called by FlexiCore before context refresh - this allows registering beans which are not extensions
     * @return a list of additional classes which are beans
     */
    public List<Class<?>> getAdditionalBeanClasses(List<Class<? extends com.flexicore.interfaces.Plugin>> discoveredClasses){
        return new ArrayList<>();
    }
}
