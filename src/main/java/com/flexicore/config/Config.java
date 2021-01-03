package com.flexicore.config;

import com.flexicore.interfaces.ServicePlugin;
import org.pf4j.Extension;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Extension
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAsync
public class Config implements ServicePlugin {



}
