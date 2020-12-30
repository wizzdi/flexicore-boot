package com.wizzdi.flexicore.boot.jaxrs.init;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.wizzdi.flexicore.boot.jaxrs","org.jboss.resteasy.springboot"})
public class JaxRsPluginHandlerModule {



}
