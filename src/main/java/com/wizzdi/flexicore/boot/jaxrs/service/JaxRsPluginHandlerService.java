package com.wizzdi.flexicore.boot.jaxrs.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.init.PluginInit;
import com.wizzdi.flexicore.boot.jaxrs.interfaces.RestServicePlugin;
import com.wizzdi.flexicore.boot.rest.interfaces.AspectPlugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class JaxRsPluginHandlerService implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(JaxRsPluginHandlerService.class);

	private static final Set<Class<?>> jaxRSClasses=new HashSet<>();

	@Autowired
	@Lazy
	private FlexiCorePluginManager pluginManager;



	public void loadRESTServices() {
		logger.info("loading jax-rs plugins");
		List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins().stream().sorted(PluginInit.PLUGIN_COMPARATOR).collect(Collectors.toList());
		List<? extends AspectPlugin> aspects = pluginManager.getExtensions(AspectPlugin.class);

		for (PluginWrapper startedPlugin : startedPlugins) {
			long start = System.currentTimeMillis();
			logger.info("REST Registration handling plugin: " + startedPlugin);
			ApplicationContext applicationContext = pluginManager.getApplicationContext(startedPlugin);
			List<? extends RestServicePlugin> restPlugins = pluginManager.getExtensions(RestServicePlugin.class, startedPlugin.getPluginId());

			for (RestServicePlugin plugin : restPlugins) {


				logger.info("REST class " + plugin);
				Class<? extends RestServicePlugin> restClass = plugin.getClass();
				try {
					Object proxy;
					if(!aspects.isEmpty()){
						AspectJProxyFactory factory = new AspectJProxyFactory(plugin);
						for (AspectPlugin aspect : aspects) {
							factory.addAspect(aspect);
						}
						factory.setProxyTargetClass(true);
						proxy = factory.getProxy(restClass.getClassLoader());
					}
					else{
						proxy=plugin;
					}


					JaxRsActivator.addSingletones(proxy);
					jaxRSClasses.add(restClass);

				} catch (Exception e) {
					logger.error("Failed registering REST service " + restClass, e);
				}


			}
			logger.debug("registering " + startedPlugin.getPluginId() + " for REST services took " + (System.currentTimeMillis() - start));

		}


	}

	public static Set<Class<?>> getJaxRSClasses() {
		return jaxRSClasses;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadRESTServices();
	}
}
