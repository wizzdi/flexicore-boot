package com.wizzdi.flexicore.common.user.config;

import com.flexicore.model.User;
import com.wizzdi.flexicore.boot.jpa.service.ReflectionRootHolder;
import com.wizzdi.flexicore.common.user.rest.CommonUserController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class CommonUserEntitiesConfig {
    @Bean
    public ReflectionRootHolder commonUserEntities(){
        return new ReflectionRootHolder(Set.of(User.class, CommonUserController.class));
    }
}
