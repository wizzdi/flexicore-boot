package com.wizzdi.dynamic.properties.converter;

import com.wizzdi.flexicore.boot.jpa.init.FlexiCoreJPAConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(FlexiCoreJPAConfiguration.class)
@ComponentScan(basePackages = {"com.wizzdi.dynamic.properties.converter"})
public class DynamicPropertiesModule  {
    @Bean
    public JsonConverterImplementationHolder jsonConverterImplementationHolder(JsonConverterImplementation jsonConverterImplementation) throws BeansException {
        JsonConverter.implementation = jsonConverterImplementation;
        return new JsonConverterImplementationHolder(jsonConverterImplementation);
    }
}
