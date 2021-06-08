package com.test.init;

import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;
import com.wizzdi.dynamic.properties.converter.JsonConverter;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesHolder;
import com.wizzdi.flexicore.file.model.FileResource;
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
        return new EntitiesHolder(new HashSet<>(Arrays.asList(Baseclass.class, JsonConverter.class, FileResource.class, FilteringInformationHolder.class)));
    }
}
