package com.wizzdi.flexicore.boot.base.pluginB;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.base.pluginA.SomeInterface;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Extension
@Component
public class PluginBService implements SomeInterface, InitializingBean {

	private static final Logger logger= LoggerFactory.getLogger(PluginBService.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("PluginBService Started!");
	}

	@Override
	public String doSomething() {
		return getClass().getCanonicalName();
	}
}
