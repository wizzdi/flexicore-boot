package com.wizzdi.flexicore.boot.base.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Extension
@Component
public class PluginAService implements Plugin, InitializingBean {

	private static final Logger logger= LoggerFactory.getLogger(PluginAService.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("PluginAService Started!");
	}
}
