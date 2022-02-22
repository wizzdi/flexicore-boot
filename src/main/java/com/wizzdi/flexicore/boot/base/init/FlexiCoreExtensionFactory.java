package com.wizzdi.flexicore.boot.base.init;

import com.wizzdi.flexicore.boot.base.exception.ContextRefreshFailedException;
import com.wizzdi.flexicore.boot.base.interfaces.ContextCustomizer;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringExtensionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class FlexiCoreExtensionFactory extends SpringExtensionFactory {

	private final FlexiCorePluginManager pluginManager;
	private final Map<String, FlexiCoreApplicationContext> contextCache = new ConcurrentHashMap<>();
	private final Queue<ApplicationContext> pluginsApplicationContexts = new LinkedBlockingQueue<>();
	private final Queue<ApplicationContext> allApplicationContext = new LinkedBlockingQueue<>();
	private final Logger logger = LoggerFactory.getLogger(FlexiCoreExtensionFactory.class);


	public FlexiCoreExtensionFactory(FlexiCorePluginManager pluginManager) {
		super(pluginManager, true);
		this.pluginManager = pluginManager;
	}


	@Override
	public <T> T create(Class<T> extensionClass) {

		PluginWrapper pluginWrapper = this.pluginManager.whichPlugin(extensionClass);
		boolean concrete = !extensionClass.isInterface();
		boolean bean = concrete && AnnotationUtils.findAnnotation(extensionClass, Component.class) != null;
		if (bean) {

			ApplicationContext pluginContext = pluginWrapper != null ? getApplicationContext(pluginWrapper) : pluginManager.getApplicationContext();
			try {
				return getBeanAccountForProxy(extensionClass, pluginContext);
			}

			catch (Throwable e){
				if(ExceptionUtils.indexOfType(e,ContextRefreshFailedException.class)!=-1){
					throw e;
				}
				if(logger.isDebugEnabled()){
					logger.debug("cannot instantiate extension",e);
				}
			}

		} else {
			if (concrete) {
				return this.createWithoutSpring(extensionClass);
			}

		}
		return null;


	}

	private <T> T getBeanAccountForProxy(Class<T> extensionClass, ApplicationContext pluginContext) {
		T bean = pluginContext.getBean(extensionClass);
		if (!ClassUtils.getUserClass(bean).equals(extensionClass)) {
			bean = this.createWithoutSpring(extensionClass);
			if (bean != null) {
				pluginContext.getAutowireCapableBeanFactory().autowireBean(bean);

			}
		}
		return bean;
	}


	public ApplicationContext getApplicationContext(PluginWrapper pluginWrapper) {
		if (pluginWrapper == null) {
			return pluginManager.getApplicationContext();
		}
		String pluginId = pluginWrapper != null ? pluginWrapper.getPluginId() : "core-extensions";
		FlexiCoreApplicationContext applicationContext = contextCache.get(pluginId);
		if (applicationContext == null) {
			logger.debug("creating context for " + pluginId);

			long start = System.currentTimeMillis();
			applicationContext = createApplicationContext(pluginWrapper);
			contextCache.put(pluginId, applicationContext);
			List<String> dependencies = pluginWrapper != null ? pluginWrapper.getDescriptor().getDependencies().parallelStream().map(f -> f.getPluginId()).sorted().collect(Collectors.toList()) : new ArrayList<>();
			List<ApplicationContext> dependenciesContexts = dependencies.stream().map(f -> pluginManager.getPlugin(f)).filter(f -> f != null).map(this::getApplicationContext).collect(Collectors.toList());
			applicationContext.getAutowireCapableBeanFactory().setDependenciesContext(getAllApplicationContext());
			for (ContextCustomizer applicationCustomizer : pluginManager.getApplicationCustomizers()) {
				applicationCustomizer.customize(applicationContext, pluginWrapper, pluginManager);
			}
			try {
				addContext(applicationContext);
				applicationContext.refresh();
				logger.debug("creating context for " + pluginId + " took " + (System.currentTimeMillis() - start) + "ms");
			}
			catch (Throwable e){
				throw new ContextRefreshFailedException(e);
			}

		}
		return applicationContext;
	}

	private void addContext(FlexiCoreApplicationContext applicationContext) {
		pluginsApplicationContexts.add(applicationContext);
		allApplicationContext.add(applicationContext);
	}

	private FlexiCoreApplicationContext createApplicationContext(PluginWrapper pluginWrapper) {
		String pluginId = pluginWrapper != null ? pluginWrapper.getPluginId() : null;
		List<Class<? extends Plugin>> beanClasses = pluginManager.getExtensionClasses(Plugin.class, pluginId);
		ClassLoader pluginClassLoader = pluginWrapper != null ? pluginWrapper.getPluginClassLoader() : Thread.currentThread().getContextClassLoader();
		FlexiCoreApplicationContext applicationContext = new FlexiCoreApplicationContext();
		applicationContext.setParent(pluginManager.getApplicationContext());
		applicationContext.setClassLoader(pluginClassLoader);
		for (Class<? extends Plugin> beanClass : beanClasses) {
			if (!beanClass.isInterface()) {
				applicationContext.register(beanClass);
			}
		}

		return applicationContext;

	}


	public Queue<ApplicationContext> getPluginsApplicationContexts() {
		return pluginsApplicationContexts;
	}

	public Queue<ApplicationContext> getAllApplicationContext() {
		return allApplicationContext;
	}

	public void init() {

		this.allApplicationContext.add(pluginManager.getApplicationContext());
	}
}
