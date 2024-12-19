package com.wizzdi.flexicore.boot.base.init;

import com.wizzdi.flexicore.boot.base.interfaces.ContextCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.io.File;

@Configuration
public class PluginLoaderConfig {

    @Value("${flexicore.plugins:/home/flexicore/plugins}")
    private String pluginsDir;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Primary
    public FlexiCorePluginManager pluginManager(@Qualifier("pluginManagerUnInjected")FlexiCorePluginManager pluginManagerUnInjected) {
          pluginManagerUnInjected.inject();
          return pluginManagerUnInjected;
    }
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("pluginManagerUnInjected")
    public FlexiCorePluginManager pluginManagerUnInjected(ObjectProvider<ContextCustomizer> contextCustomizers, ApplicationContext applicationContext) {
        return new FlexiCorePluginManager(new File(pluginsDir).toPath(),contextCustomizers);
    }



}
