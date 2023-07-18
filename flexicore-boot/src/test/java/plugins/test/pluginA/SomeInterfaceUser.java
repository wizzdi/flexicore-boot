package plugins.test.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Extension
@Component
public class SomeInterfaceUser implements Plugin, InitializingBean {

	private static final Logger logger= LoggerFactory.getLogger(SomeInterfaceUser.class);

	@Autowired
	private ObjectProvider<SomeInterface> someInterfaces;
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("got SomeInterface implementors: "+someInterfaces.stream().map(f->f.doSomething()).collect(Collectors.joining(",")));
	}
}
