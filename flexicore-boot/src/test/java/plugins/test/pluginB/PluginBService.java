package plugins.test.pluginB;

import plugins.test.pluginA.PluginAService;
import plugins.test.pluginA.SomeInterface;
import plugins.test.pluginA.SomeInterfaceUser;
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
		System.out.println("PluginBService Started!"+pluginAService.doSomething() );
	}

	@Override
	public String doSomething() {
		return getClass().getCanonicalName();
	}
}
