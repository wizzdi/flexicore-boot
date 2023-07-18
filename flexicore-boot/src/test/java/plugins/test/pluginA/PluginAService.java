package plugins.test.pluginA;

import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Extension
@Component
public class PluginAService implements SomeInterface, InitializingBean {

	private static final Logger logger= LoggerFactory.getLogger(PluginAService.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("PluginAService Started!");
	}

	@Override
	public String doSomething() {
		return getClass().getCanonicalName();
	}
}
