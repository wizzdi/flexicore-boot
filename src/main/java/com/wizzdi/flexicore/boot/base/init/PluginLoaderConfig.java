package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class PluginLoaderConfig {

    @Value("${flexicore.plugins:/home/flexicore/plugins}")
    private String pluginsDir;

    @Bean
    public FlexiCorePluginManager pluginManager() {
        return new FlexiCorePluginManager(new File(pluginsDir).toPath());
    }



}