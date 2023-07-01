package com.wizzdi.flexicore.boot.data.rest.app;

import com.wizzdi.flexicore.boot.jpa.service.EntitiesHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
public class EntitiesConfig {

    @Bean
    public EntitiesHolder entitiesHolder(){
        return new EntitiesHolder(new HashSet<>(Arrays.asList(Book.class)));
    }
}
