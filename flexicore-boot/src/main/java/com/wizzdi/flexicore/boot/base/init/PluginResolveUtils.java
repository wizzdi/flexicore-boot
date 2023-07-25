package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.NamedBeanHolder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Set;

public class PluginResolveUtils {
    public static Object resolveDependency(AutowireCapableBeanFactory applicationContext, DependencyDescriptor descriptorToUse, String beanName, Set<String> autowiredBeanNames, TypeConverter typeConverter) {
        if(applicationContext instanceof FlexiCoreAppBeanFactory flexiCoreAppBeanFactory){
            return flexiCoreAppBeanFactory.resolveDependencyDirect(descriptorToUse, beanName, autowiredBeanNames, typeConverter);
        }
        if(applicationContext instanceof FlexiCorePluginBeanFactory flexiCorePluginBeanFactory){
            return flexiCorePluginBeanFactory.resolveDependencyDirect(descriptorToUse, beanName, autowiredBeanNames, typeConverter);
        }
        if(applicationContext instanceof DefaultListableBeanFactory defaultListableBeanFactory){
            return defaultListableBeanFactory.resolveDependency(descriptorToUse, beanName, autowiredBeanNames, typeConverter);
        }
        return null;
    }

    public static <T> NamedBeanHolder<T> resolveNamedBean(AutowireCapableBeanFactory autowireCapableBeanFactory, Class<T> requiredType) {
        if( autowireCapableBeanFactory instanceof FlexiCorePluginBeanFactory flexiCorePluginBeanFactory){
            return flexiCorePluginBeanFactory.resolveNamedBeanDirect(requiredType);
        }
        if(autowireCapableBeanFactory instanceof FlexiCoreAppBeanFactory flexiCoreAppBeanFactory){
            return flexiCoreAppBeanFactory.resolveNamedBeanDirect(requiredType);
        }
        return autowireCapableBeanFactory.resolveNamedBean(requiredType);
    }
}
