package com.wizzdi.flexicore.boot.health.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.init.PluginInit;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class HealthPluginHandlerService implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(HealthPluginHandlerService.class);


	@Autowired
	@Lazy
	private FlexiCorePluginManager pluginManager;
	@Autowired
	private HealthContributorRegistry healthContributorRegistry;

	public void loadRESTServices() {
		logger.info("loading health plugins");
		List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins().stream().sorted(PluginInit.PLUGIN_COMPARATOR).collect(Collectors.toList());
		for (PluginWrapper startedPlugin : startedPlugins) {
			long start = System.currentTimeMillis();
			logger.info("health Registration handling plugin: " + startedPlugin);
			ApplicationContext applicationContext = pluginManager.getApplicationContext(startedPlugin);
			Map<String, HealthContributor> springControllers = applicationContext.getBeansOfType(HealthContributor.class);
			for (HealthContributor healthContributor : springControllers.values()) {
				try {
					logger.info("Health class " + healthContributor.getClass().getCanonicalName());

					healthContributorRegistry.registerContributor(healthContributor.getClass().getSimpleName(), healthContributor);
				} catch (Exception e) {
					logger.error("Failed registering Health " + healthContributor.getClass(), e);

				}
			}

			logger.debug("registering " + startedPlugin.getPluginId() + " for health services took " + (System.currentTimeMillis() - start));

		}

	}


	@Override
	public void afterPropertiesSet() throws Exception {
		loadRESTServices();
	}
}
