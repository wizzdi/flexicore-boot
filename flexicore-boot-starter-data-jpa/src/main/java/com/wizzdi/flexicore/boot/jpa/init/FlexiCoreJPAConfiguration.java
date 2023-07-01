package com.wizzdi.flexicore.boot.jpa.init;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.wizzdi.flexicore.boot.jpa")
@EnableTransactionManagement(proxyTargetClass = true)
public class FlexiCoreJPAConfiguration {



}
