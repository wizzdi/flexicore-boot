package com.wizzdi.flexicore.boot.data.rest.init;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@ComponentScan(basePackages = "com.wizzdi.flexicore.boot.data.rest")
@AutoConfigureBefore(RepositoryRestMvcConfiguration.class)
public class DataRESTPluginHandlerModule {




}
