package com.wizzdi.security.bearer.jwt;

import com.wizzdi.security.adapter.EnableFlexiCoreAuthentication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFlexiCoreAuthentication
@ComponentScan(basePackageClasses = FlexiCoreJWTSecurityModule.class)
public class FlexiCoreJWTSecurityModule {
}
