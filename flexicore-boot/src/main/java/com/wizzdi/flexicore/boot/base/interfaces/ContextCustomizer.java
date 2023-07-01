package com.wizzdi.flexicore.boot.base.interfaces;

import com.wizzdi.flexicore.boot.base.init.FlexiCoreApplicationContext;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

public interface ContextCustomizer {

	void customize(FlexiCoreApplicationContext applicationContext, PluginWrapper pluginWrapper, PluginManager pluginManager);
}
