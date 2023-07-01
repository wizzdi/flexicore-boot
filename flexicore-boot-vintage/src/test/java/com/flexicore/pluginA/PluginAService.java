package com.flexicore.pluginA;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.ServicePlugin;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Component
@Extension
public class PluginAService implements ServicePlugin {


	public String test(String name) {
		return "hello" +name;
	}
}
