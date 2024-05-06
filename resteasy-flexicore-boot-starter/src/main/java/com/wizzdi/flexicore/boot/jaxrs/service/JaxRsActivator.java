/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.flexicore.boot.jaxrs.service;

import com.wizzdi.flexicore.boot.jaxrs.utils.ObjectMapperContextResolver;
import org.jboss.resteasy.core.ResourceMethodRegistry;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@ApplicationPath("/FlexiCore/rest")
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class JaxRsActivator extends Application {


    private static final Queue<Class<?>> more = new LinkedBlockingQueue<>();
    private static Queue<Class<?>> providers = new LinkedBlockingQueue<>();
    private static ResourceMethodRegistry registry;
    private static ResteasyProviderFactory resteasyProviderFactory;

    private static final Logger logger= LoggerFactory.getLogger(JaxRsActivator.class);


    private static Set<Object> singletons=Collections.synchronizedSet(new LinkedHashSet<>());

    public JaxRsActivator() {
        singletons.add(new ObjectMapperContextResolver());
    }






    public static void add(Class<?> c) {
        more.add(c);
    }

    public static void remove(Class<?> c) {
        if (registry != null) {
            registry.removeRegistrations(c);
        }
        more.remove(c);
    }



    public static Set<Class<?>> getAll() {
		return singletons.stream().map(Object::getClass).collect(Collectors.toCollection(LinkedHashSet::new));
	}

    public static void addProvider(Class<?> provider) {
        providers.add(provider);

    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons=new LinkedHashSet<>(super.getSingletons());
        singletons.addAll(JaxRsActivator.singletons);
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes=new LinkedHashSet<>(super.getClasses());
        classes.addAll(more);
        return classes;
    }

    public static void addSingletones(Object o){
        singletons.add(o);
    }
}
