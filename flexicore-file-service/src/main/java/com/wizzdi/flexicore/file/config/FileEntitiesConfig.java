package com.wizzdi.flexicore.file.config;

import com.wizzdi.flexicore.boot.jpa.service.EntitiesRootHolder;
import com.wizzdi.flexicore.file.model.FileResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class FileEntitiesConfig {

    @Bean
    public EntitiesRootHolder fileEntitiesHolder(){
        return new EntitiesRootHolder(Set.of(FileResource.class));

    }
}
