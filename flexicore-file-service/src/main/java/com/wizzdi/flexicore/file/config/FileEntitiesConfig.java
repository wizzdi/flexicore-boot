package com.wizzdi.flexicore.file.config;

import com.wizzdi.flexicore.boot.jpa.service.ReflectionRootHolder;
import com.wizzdi.flexicore.file.controllers.FileResourceController;
import com.wizzdi.flexicore.file.model.FileResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class FileEntitiesConfig {

    @Bean
    public ReflectionRootHolder fileEntitiesHolder(){
        return new ReflectionRootHolder(Set.of(FileResource.class, FileResourceController.class));

    }
}
