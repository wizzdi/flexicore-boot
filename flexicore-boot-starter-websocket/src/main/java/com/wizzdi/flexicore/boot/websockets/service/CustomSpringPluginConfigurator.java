package com.wizzdi.flexicore.boot.websockets.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.rest.interfaces.AspectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import jakarta.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomSpringPluginConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(CustomSpringPluginConfigurator.class);
	/**
	 * Spring application context.
	 */
	private static volatile BeanFactory context;

	@Override
	public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
		try {
			FlexiCorePluginManager pluginManager = context.getBean(FlexiCorePluginManager.class);
			ApplicationContext applicationContext = pluginManager.getApplicationContext(clazz);
			List<AspectPlugin> aspects = pluginManager.getExtensions(AspectPlugin.class).stream().filter(f->f!=null).collect(Collectors.toList());
			T bean = applicationContext.getBean(clazz);
			if(!aspects.isEmpty()){
				AspectJProxyFactory factory = new AspectJProxyFactory(bean);
				for (AspectPlugin aspect : aspects) {
					factory.addAspect(aspect);
				}
				factory.setProxyTargetClass(true);
				bean=factory.getProxy(clazz.getClassLoader());

			}
			return bean;

		} catch (Exception e) {
			logger.error("failed getting endpoint instance",e);
			return null;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CustomSpringPluginConfigurator.context = applicationContext;
	}

}