package com.wizzdi.flexicore.boot.base.init;

import com.wizzdi.flexicore.boot.base.interfaces.ContextCustomizer;
import org.pf4j.*;
import org.pf4j.spring.SpringPluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class FlexiCorePluginManager extends SpringPluginManager {

	private static final Logger logger = LoggerFactory.getLogger(FlexiCorePluginManager.class);
	private static final AtomicBoolean init = new AtomicBoolean(false);
	private static final AtomicBoolean inject=new AtomicBoolean(false);
	private Iterable<ContextCustomizer> applicationCustomizers;


	public FlexiCorePluginManager() {
	}

	public FlexiCorePluginManager(Path pluginsRoot, Iterable<ContextCustomizer> applicationCustomizers) {
		super(pluginsRoot);
		this.applicationCustomizers = applicationCustomizers;
	}

	@Override
	public List<Class<?>> getExtensionClasses(String pluginId) {
		try {
			return super.getExtensionClasses(pluginId);
		}
		catch (Throwable e){
			logger.error("error while loading plugin {}",pluginId,e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected ExtensionFactory createExtensionFactory() {
		return new FlexiCoreExtensionFactory(this);
	}

	@Override
	protected VersionManager createVersionManager() {
		return new ImprovedVersionManager();
	}

	public Iterable<ContextCustomizer> getApplicationCustomizers() {
		return applicationCustomizers;
	}

	public Collection<ApplicationContext> getPluginApplicationContexts() {
		FlexiCoreExtensionFactory extensionFactory = (FlexiCoreExtensionFactory) getExtensionFactory();
		return Collections.unmodifiableCollection(extensionFactory.getPluginsApplicationContexts());
	}

	public Collection<ApplicationContext> getAllApplicationContexts() {
		FlexiCoreExtensionFactory extensionFactory = (FlexiCoreExtensionFactory) getExtensionFactory();
		return Collections.unmodifiableCollection(extensionFactory.getAllApplicationContext());
	}

	public ApplicationContext getApplicationContext(Class<?> c) {
		FlexiCoreExtensionFactory extensionFactory = (FlexiCoreExtensionFactory) getExtensionFactory();
		PluginWrapper pluginWrapper = whichPlugin(c);
		return pluginWrapper == null ? getApplicationContext() : extensionFactory.getApplicationContext(pluginWrapper);
	}

	public ApplicationContext getApplicationContext(PluginWrapper pluginWrapper) {
		FlexiCoreExtensionFactory extensionFactory = (FlexiCoreExtensionFactory) getExtensionFactory();
		return pluginWrapper == null ? getApplicationContext() : extensionFactory.getApplicationContext(pluginWrapper);

	}


	@Override
	protected PluginDescriptorFinder createPluginDescriptorFinder() {
		return new ManifestPluginDescriptorFinder();
	}

	@Override
	protected PluginLoader createPluginLoader() {
		return new CompoundPluginLoader()
				.add(new DevelopmentPluginLoader(this), this::isDevelopment)
				.add(new FlexiCoreJarPluginLoader(this), this::isNotDevelopment)
				.add(new DefaultPluginLoader(this), this::isNotDevelopment);
	}

	@Override
	public void init() {
		if (init.compareAndSet(false, true)) {
			long start = System.currentTimeMillis();
			try {
				this.loadPlugins();
			} catch (DependencyResolver.DependenciesWrongVersionException e) {
				List<DependencyResolver.WrongDependencyVersion> dependencies = e.getDependencies();
                logger.error("loading plugins failed , wrong versions: {}", dependencies.stream().map(f -> getWrongDependencyString(f)).collect(Collectors.joining(System.lineSeparator())), e);
				throw e;
			}
			catch (DependencyResolver.DependenciesNotFoundException e) {
				Set<String> dependencies = new HashSet<>(e.getDependencies());
				List<String> requiredBy=plugins.values().stream().filter(f->f.getDescriptor().getDependencies().stream().anyMatch(x->dependencies.contains(x.getPluginId()))).map(f->f.getPluginId()).toList();
				logger.error("missing dependencies {} required by {}",dependencies,requiredBy);
				throw e;
			}

			this.startPlugins();
            logger.debug("loading and starting plugins took {}ms", System.currentTimeMillis() - start);

		}

	}

	public void inject(){
		if(inject.compareAndSet(false,true)){
			long start=System.currentTimeMillis();
			FlexiCoreExtensionFactory extensionFactory = (FlexiCoreExtensionFactory) getExtensionFactory();
			extensionFactory.init();
			AbstractAutowireCapableBeanFactory beanFactory = (AbstractAutowireCapableBeanFactory) getApplicationContext().getAutowireCapableBeanFactory();
			FlexiCoreExtensionsInjector extensionsInjector = new FlexiCoreExtensionsInjector(this, beanFactory);
			extensionsInjector.injectExtensions();

			logger.debug("injecting plugins took {}ms", System.currentTimeMillis() - start);
		}
	}

	private String getWrongDependencyString(DependencyResolver.WrongDependencyVersion wrongDependencyVersion) {
		return "plugin " + wrongDependencyVersion.getDependentId() + " required " + wrongDependencyVersion.getDependencyId() + " version: " + wrongDependencyVersion.getRequiredVersion() + ", actual: " + wrongDependencyVersion.getExistingVersion();
	}
}
