package com.wizzdi.security.adapter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class DefaultSecurityPathProvider {

    @Bean
    @Order(100)
    @ConditionalOnMissingBean(SecurityPathConfigurator.class)
    public SecurityPathConfigurator securityPathConfigurator(){
        return expressionInterceptUrlRegistry->expressionInterceptUrlRegistry.anyRequest().authenticated();
    }
}
