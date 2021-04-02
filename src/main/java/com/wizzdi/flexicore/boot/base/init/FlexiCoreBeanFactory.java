package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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
        try {
            return super.resolveDependency(descriptor, requestingBeanName, autowiredBeanNames, typeConverter);
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
        return getBeanProvider(requiredType,new HashSet<>());
    }


    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType,Set<String> visitedContext) throws BeansException {
        ObjectProvider<T> beanProvider = super.getBeanProvider(requiredType);

        return new ObjectProvider<T>() {
            @Override
            public T getObject() throws BeansException {
                try {
                    return beanProvider.getObject();
                } catch (NoSuchBeanDefinitionException e) {
                    for (ApplicationContext applicationContext : dependenciesContext) {
                        try {
                            if(visitedContext.add(applicationContext.getId())){
                                AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                                ObjectProvider<T> objectProvider = autowireCapableBeanFactory instanceof FlexiCoreBeanFactory ? ((FlexiCoreBeanFactory) autowireCapableBeanFactory).getBeanProvider(requiredType, visitedContext) : autowireCapableBeanFactory.getBeanProvider(requiredType);
                                return objectProvider.getObject();
                            }

                        } catch (NoSuchBeanDefinitionException e1) {
                        }
                    }
                    throw e;
                }
            }

            @Override
            public T getObject(Object... args) throws BeansException {
                try {
                    return beanProvider.getObject(args);
                } catch (NoSuchBeanDefinitionException e) {
                    for (ApplicationContext applicationContext : dependenciesContext) {
                        try {
                            if(visitedContext.add(applicationContext.getId())){
                                AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                                ObjectProvider<T> objectProvider = autowireCapableBeanFactory instanceof FlexiCoreBeanFactory ? ((FlexiCoreBeanFactory) autowireCapableBeanFactory).getBeanProvider(requiredType, visitedContext) : autowireCapableBeanFactory.getBeanProvider(requiredType);
                                return objectProvider.getObject(args);
                            }

                        } catch (NoSuchBeanDefinitionException e1) {
                        }
                    }
                    throw e;
                }
            }

            @Override
            @Nullable
            public T getIfAvailable() throws BeansException {

                T t = beanProvider.getIfAvailable();
                if (t == null) {
                    for (ApplicationContext applicationContext : dependenciesContext) {
                        if(visitedContext.add(applicationContext.getId())){
                            AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                            ObjectProvider<T> objectProvider = autowireCapableBeanFactory instanceof FlexiCoreBeanFactory ? ((FlexiCoreBeanFactory) autowireCapableBeanFactory).getBeanProvider(requiredType, visitedContext) : autowireCapableBeanFactory.getBeanProvider(requiredType);
                            t = objectProvider.getIfAvailable();
                            if (t != null) {
                                return t;
                            }
                        }


                    }
                }
                return t;
            }

            @Override
            @Nullable
            public T getIfUnique() throws BeansException {
                T t = beanProvider.getIfUnique();
                if (t == null) {
                    for (ApplicationContext applicationContext : dependenciesContext) {
                        if(visitedContext.add(applicationContext.getId())){
                            AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                            ObjectProvider<T> objectProvider = autowireCapableBeanFactory instanceof FlexiCoreBeanFactory ? ((FlexiCoreBeanFactory) autowireCapableBeanFactory).getBeanProvider(requiredType, visitedContext) : autowireCapableBeanFactory.getBeanProvider(requiredType);
                            t = objectProvider.getIfUnique();
                            if (t != null) {
                                return t;
                            }
                        }


                    }
                }
                return t;
            }

            @Override
            public Stream<T> stream() {
                Stream<T> stream = beanProvider.stream();
                for (ApplicationContext applicationContext : dependenciesContext) {
                    if(visitedContext.add(applicationContext.getId())){
                        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                        ObjectProvider<T> objectProvider = autowireCapableBeanFactory instanceof FlexiCoreBeanFactory ? ((FlexiCoreBeanFactory) autowireCapableBeanFactory).getBeanProvider(requiredType, visitedContext) : autowireCapableBeanFactory.getBeanProvider(requiredType);
                        stream = Stream.concat(stream,objectProvider.stream());
                    }

                }
                return stream;

            }

            @Override
            public Stream<T> orderedStream() {
                Stream<T> stream = beanProvider.orderedStream();
                for (ApplicationContext applicationContext : dependenciesContext) {
                    if(visitedContext.add(applicationContext.getId())){
                        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
                        ObjectProvider<T> objectProvider = autowireCapableBeanFactory instanceof FlexiCoreBeanFactory ? ((FlexiCoreBeanFactory) autowireCapableBeanFactory).getBeanProvider(requiredType, visitedContext) : autowireCapableBeanFactory.getBeanProvider(requiredType);
                        stream = Stream.concat(stream,objectProvider.orderedStream());
                    }
                }
                return stream;
            }
        };
    }
}
