package com.wizzdi.flexicore.boot.jpa.hibernate.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Extension
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class Config implements Plugin {


}
