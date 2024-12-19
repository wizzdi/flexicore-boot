package com.wizzdi.flexicore.boot.dynamic.invokers.config;

import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesRootHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class DynamicInvokersEntitiesConfig {

    @Bean
    public EntitiesRootHolder dynamicInvokersEntitiesHolder(){
        return new EntitiesRootHolder(Set.of(DynamicExecution.class));

    }
}
