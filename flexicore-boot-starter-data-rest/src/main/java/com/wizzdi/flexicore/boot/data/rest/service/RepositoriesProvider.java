package com.wizzdi.flexicore.boot.data.rest.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.support.Repositories;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RepositoriesProvider {

    private static final Logger logger= LoggerFactory.getLogger(RepositoriesProvider.class);

    @Bean
    @Primary
    public Repositories repositories2(FlexiCorePluginManager pluginManager, ApplicationContext applicationContext){
         List<Repositories> repositories = pluginManager.getPluginApplicationContexts().stream().map(f -> new Repositories(f)).collect(Collectors.toList());
        logger.info("creating combined repositories from existing: "+ repositories.size());
        return new CombinedRepositories(applicationContext,repositories);
    }



}
