package plugins.test.pluginF;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import plugins.test.pluginA.SomeInterface;

import java.util.stream.Collectors;

@Extension
@Component
public class PluginFService implements Plugin,  InitializingBean {

	private static final Logger logger= LoggerFactory.getLogger(PluginFService.class);
	@Autowired
	private ObjectProvider<SomeInterface> someInterfaceObjectProvider;


	@Override
	public void afterPropertiesSet() throws Exception {
		String names = someInterfaceObjectProvider.stream().toList().stream().map(f -> f.getClass().getSimpleName()).collect(Collectors.joining(","));
		System.out.println("PluginFService Started!: "+names+" SomeInterface");
	}

}
