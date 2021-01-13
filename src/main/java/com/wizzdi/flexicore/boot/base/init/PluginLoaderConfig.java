package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;

@Configuration
public class PluginLoaderConfig {

    @Value("${flexicore.plugins:/home/flexicore/plugins}")
    private String pluginsDir;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public FlexiCorePluginManager pluginManager() {
        return new FlexiCorePluginManager(new File(pluginsDir).toPath());
    }



}