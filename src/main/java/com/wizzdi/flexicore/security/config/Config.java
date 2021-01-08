package com.wizzdi.flexicore.security.config;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement(proxyTargetClass = true)
public class Config implements Plugin {
}
