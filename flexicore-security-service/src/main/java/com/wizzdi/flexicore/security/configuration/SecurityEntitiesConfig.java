package com.wizzdi.flexicore.security.configuration;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.security.SecurityPolicy;
import com.wizzdi.flexicore.boot.jpa.service.ReflectionRootHolder;
import com.wizzdi.flexicore.security.rest.BaseclassController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class SecurityEntitiesConfig {


    @Bean
    public ReflectionRootHolder securityEntitiesHolder(){
        return new ReflectionRootHolder(new HashSet<>(Arrays.asList(Baseclass.class, Basic.class, SecurityPolicy.class, BaseclassController.class)));
    }
}
