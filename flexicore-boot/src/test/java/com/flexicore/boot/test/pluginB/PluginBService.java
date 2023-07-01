package com.flexicore.boot.test.pluginB;

import com.flexicore.boot.test.pluginA.PluginAService;
import com.flexicore.boot.test.pluginA.SomeInterface;
import com.flexicore.boot.test.pluginA.SomeInterfaceUser;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Extension
@Component
public class PluginBService implements SomeInterface, InitializingBean {

	private static final Logger logger= LoggerFactory.getLogger(PluginBService.class);
	@Autowired
	private PluginAService pluginAService;
	@Autowired
	@Lazy
	private SomeInterfaceUser someInterfaceUser;

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("PluginBService Started!"+pluginAService.doSomething() +" lazy: "+someInterfaceUser.toString());
	}

	@Override
	public String doSomething() {
		return getClass().getCanonicalName();
	}
}
