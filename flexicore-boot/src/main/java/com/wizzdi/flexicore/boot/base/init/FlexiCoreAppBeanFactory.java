package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.NamedBeanHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class FlexiCoreAppBeanFactory extends DefaultListableBeanFactory {

    private ApplicationContext applicationContext;
    private FlexiCorePluginManager flexiCorePluginManager;
    private final AtomicBoolean init = new AtomicBoolean(false);
    private Queue<ApplicationContext> dependenciesContext = new LinkedBlockingQueue<>();


    public FlexiCoreAppBeanFactory() {
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public <T extends FlexiCoreAppBeanFactory> T setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        return (T) this;
    }

    public void setDependenciesContext(Queue<ApplicationContext> dependenciesContext) {
        this.dependenciesContext = dependenciesContext;
    }


    private void init() {
        if(isConfigurationFrozen()){
            if (init.compareAndSet(false, true)) {
                String pluginPath = getApplicationContext().getEnvironment().getProperty("flexicore.plugins","/home/flexicore/plugins");
                flexiCorePluginManager = new FlexiCorePluginManager(Path.of(pluginPath), Collections.emptyList());
                flexiCorePluginManager.setApplicationContext(this.applicationContext);
                flexiCorePluginManager.init();
                logger.info("plugins init complete");
                dependenciesContext.addAll(flexiCorePluginManager.getPluginApplicationContexts());
            }
        }

    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
        init();
        Map<String, T> beansOfType = super.getBeansOfType(type, includeNonSingletons, allowEagerInit);
        for (ApplicationContext applicationContext : dependenciesContext) {
            beansOfType.putAll(applicationContext.getBeansOfType(type, includeNonSingletons, allowEagerInit));
        }
        return beansOfType;
    }

    public Object resolveDependencyDirect(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
        init();
        return super.resolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
    }


    @Override
    public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {


        try {
            return super.resolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
        } catch (BeansException e) {
            init();
            for (ApplicationContext applicationContext : dependenciesContext) {
                try {
                    AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                    return PluginResolveUtils.resolveDependency(autowireCapableBeanFactory,descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
                } catch (BeansException ignored) {

                }


            }
            throw e;
        }

    }
    public <T> NamedBeanHolder<T> resolveNamedBeanDirect(Class<T> requiredType) throws BeansException {
        return super.resolveNamedBean(requiredType);
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) throws BeansException {

        ObjectProvider<T> beanProvider = super.getBeanProvider(requiredType);
        init();
        return new FlexiCoreObjectProvider<>(requiredType, beanProvider, dependenciesContext);
    }

    public Queue<ApplicationContext> getDependenciesContext() {
        return dependenciesContext;
    }

    public FlexiCorePluginManager getFlexiCorePluginManager() {
        init();
        return flexiCorePluginManager;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return super.getBeanDefinitionNames();
    }
}
