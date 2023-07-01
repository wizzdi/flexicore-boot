package com.wizzdi.flexicore.boot.rest.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.init.PluginInit;
import com.wizzdi.flexicore.boot.rest.interfaces.AspectPlugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class RESTPluginHandlerService  {

	private static final Logger logger= LoggerFactory.getLogger(RESTPluginHandlerService.class);



	@Bean
	public RestServicesContext loadRESTServices(FlexiCorePluginManager flexiCorePluginManager,RequestMappingHandlerMapping requestMappingHandlerMapping){
		logger.info("loading rest plugins");

		List<PluginWrapper> startedPlugins = flexiCorePluginManager.getStartedPlugins().stream().sorted(PluginInit.PLUGIN_COMPARATOR).collect(Collectors.toList());
		List<? extends AspectPlugin> aspects = flexiCorePluginManager.getExtensions(AspectPlugin.class);

		for (PluginWrapper startedPlugin : startedPlugins) {
			long start = System.currentTimeMillis();
			logger.info("REST Registration handling plugin: " + startedPlugin);
			ApplicationContext applicationContext = flexiCorePluginManager.getApplicationContext(startedPlugin);
			Map<String, Object> springControllers = applicationContext.getBeansWithAnnotation(RestController.class);
			for (Object plugin : springControllers.values()) {
				try {
					Object proxy;
					Class<?> restClass = plugin.getClass();
					if (!aspects.isEmpty()) {
						logger.debug("rest class "+restClass+" will be proxied with aspects");
						AspectJProxyFactory factory = new AspectJProxyFactory(plugin);
						for (AspectPlugin aspect : aspects) {
							factory.addAspect(aspect);
						}
						factory.setProxyTargetClass(true);
						proxy = factory.getProxy(restClass.getClassLoader());
					} else {
						logger.debug("rest class "+restClass+" will NOT be proxied with aspects");
						proxy = plugin;
					}

					registerSpringRESTController(proxy, restClass,requestMappingHandlerMapping);
				}
				catch (Exception e){
					logger.error("failed registering rest class",e);
				}

			}

			logger.debug("registering "+startedPlugin.getPluginId() +" for REST services took "+(System.currentTimeMillis()-start));

		}
		return new RestServicesContext();


	}

	private void registerSpringRESTController(Object plugin, Class<?> originalClass, RequestMappingHandlerMapping requestMappingHandlerMappinga) {
		CustomRequestMappingHandlerMapping requestMappingHandlerMapping= (CustomRequestMappingHandlerMapping) requestMappingHandlerMappinga;
		Class<?> pluginClass = plugin.getClass();
		for (Method method : originalClass.getMethods()) {
			RequestMappingInfo mappingForMethod = requestMappingHandlerMapping.getMappingForMethod(method, pluginClass);
			if(mappingForMethod!=null){
				requestMappingHandlerMapping.registerHandlerMethod(plugin,method,mappingForMethod);
			}

		}
	}


}
