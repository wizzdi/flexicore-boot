package com.wizzdi.flexicore.boot.dynamic.invokers.service.app;

import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.InvokerBodyConverter;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class EntitiesSeedProvider {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)

    public EntitiesHolder manualEntityHolder(){
        return new EntitiesHolder(new HashSet<>(Arrays.asList(Baseclass.class, DynamicExecution.class, InvokerBodyConverter.class)));
    }
}
