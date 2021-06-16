package com.wizzdi.dynamic.properties.converter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.wizzdi.dynamic.properties.converter"})
public class DynamicPropertiesModule implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JsonConverter.implementation = applicationContext.getBean(JsonConverterImplementation.class);
    }
}
