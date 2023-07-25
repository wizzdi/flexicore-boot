package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.NamedBeanHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class FlexiCorePluginBeanFactory extends DefaultListableBeanFactory {

    private Queue<ApplicationContext> dependenciesContext=new LinkedBlockingQueue<>();

    public FlexiCorePluginBeanFactory() {
    }


    public void setDependenciesContext(Queue<ApplicationContext> dependenciesContext){
        this.dependenciesContext=dependenciesContext;
    }




    public Object resolveDependencyDirect(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
        return super.resolveDependency(descriptor,requestingBeanName,autowiredBeanNames,typeConverter);
    }

    public <T> NamedBeanHolder<T> resolveNamedBeanDirect(Class<T> requiredType) throws BeansException {
        return super.resolveNamedBean(requiredType);
    }

    @Override
    public <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException {
        try {
            return super.resolveNamedBean(requiredType);
        }
        catch (BeansException e){
            for (ApplicationContext applicationContext   : dependenciesContext) {
                try {
                    AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                    return PluginResolveUtils.resolveNamedBean(autowireCapableBeanFactory,requiredType);
                }
                catch (BeansException ignored){

                }


            }
            throw e;
        }
    }

    @Override
    public Object resolveDependency(DependencyDescriptor descriptor, String requestingBeanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {


        if (ObjectFactory.class == descriptor.getDependencyType() ||
                ObjectProvider.class == descriptor.getDependencyType()) {
            return new FlexiCoreDependencyObjectProvider(this,dependenciesContext,descriptor, requestingBeanName);
        }

        try {
            return  super.resolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
        }
        catch (BeansException e){
            for (ApplicationContext applicationContext   : dependenciesContext) {
                try {
                    AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                    return PluginResolveUtils.resolveDependency(autowireCapableBeanFactory,descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
                }
                catch (BeansException ignored){

                }


            }
            throw e;
        }

    }


    public Queue<ApplicationContext> getDependenciesContext() {
        return dependenciesContext;
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) throws BeansException {
        ObjectProvider<T> beanProvider = super.getBeanProvider(requiredType);

        return new FlexiCoreObjectProvider<>(requiredType,beanProvider,dependenciesContext);
    }
}
