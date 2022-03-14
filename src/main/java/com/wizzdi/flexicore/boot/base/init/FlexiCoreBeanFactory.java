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

    private Queue<ApplicationContext> dependenciesContext = new LinkedBlockingQueue<>();

    public FlexiCoreBeanFactory() {
    }

    public FlexiCoreBeanFactory(BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
    }

    public void setDependenciesContext(Queue<ApplicationContext> dependenciesContext) {
        this.dependenciesContext = dependenciesContext;
    }



    @Override
    public Object doResolveDependency(DependencyDescriptor descriptor, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException {
        try {
            return super.doResolveDependency(descriptor, beanName, autowiredBeanNames, typeConverter);
        } catch (BeansException e) {

            if (!(descriptor instanceof FlexicoreDependencyDescriptor)) {
                descriptor = new FlexicoreDependencyDescriptor(descriptor);
            }
            FlexicoreDependencyDescriptor flexicoreDependencyDescriptor = (FlexicoreDependencyDescriptor) descriptor;

            for (ApplicationContext applicationContext : dependenciesContext) {
                try {
                    AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                    if (autowireCapableBeanFactory instanceof FlexiCoreBeanFactory) {
                        FlexiCoreBeanFactory flexiCoreBeanFactory = (FlexiCoreBeanFactory) autowireCapableBeanFactory;
                        if (flexicoreDependencyDescriptor.addVisitedContextId(applicationContext.getId())) {
                            return flexiCoreBeanFactory.doResolveDependency(descriptor, beanName, autowiredBeanNames, typeConverter);
                        }
                    }
                } catch (BeansException ignored) {

                }


            }
            throw e;
        }
    }

    public Queue<ApplicationContext> getDependenciesContext() {
        return dependenciesContext;
    }


}
