package com.wizzdi.flexicore.security.init;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.wizzdi.flexicore.security")
@Extension
@EnableTransactionManagement(proxyTargetClass = true)
public class FlexiCoreSecurityModule implements Plugin {






}
