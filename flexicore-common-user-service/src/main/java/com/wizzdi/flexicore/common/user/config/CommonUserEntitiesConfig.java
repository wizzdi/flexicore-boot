package com.wizzdi.flexicore.common.user.config;

import com.flexicore.model.User;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesRootHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class CommonUserEntitiesConfig {
    @Bean
    public EntitiesRootHolder commonUserEntities(){
        return new EntitiesRootHolder(Set.of(User.class));
    }
}
