package com.wizzdi.flexicore.boot.dynamic.invokers.config;

import com.wizzdi.flexicore.boot.dynamic.invokers.controllers.DynamicExecutionController;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.jpa.service.ReflectionRootHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class DynamicInvokersEntitiesConfig {

    @Bean
    public ReflectionRootHolder dynamicInvokersEntitiesHolder(){
        return new ReflectionRootHolder(Set.of(DynamicExecution.class, DynamicExecutionController.class));

    }
}
