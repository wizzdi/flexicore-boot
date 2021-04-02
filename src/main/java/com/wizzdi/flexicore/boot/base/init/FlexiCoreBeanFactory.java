package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class FlexiCoreBeanFactory extends DefaultListableBeanFactory {

    private Queue<ApplicationContext> dependenciesContext=new LinkedBlockingQueue<>();

    public FlexiCoreBeanFactory() {
    }

    public FlexiCoreBeanFactory(BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
    }

    public void setDependenciesContext(Queue<ApplicationContext> dependenciesContext){
        this.dependenciesContext=dependenciesContext;
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
            for (ApplicationContext applicationContext : dependenciesContext) {
                try {
                    return applicationContext.getAutowireCapableBeanFactory().resolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
                }
                catch (BeansException ignored){

                }
            }
            throw e;
        }

    }




    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) throws BeansException {
        ObjectProvider<T> beanProvider = super.getBeanProvider(requiredType);

        return new FlexiCoreObjectProvider<>(requiredType,beanProvider,dependenciesContext);
    }
}
