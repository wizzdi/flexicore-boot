package com.wizzdi.security.adapter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(FlexiCoreSecurityModule.class)
@Configuration
public @interface EnableFlexiCoreAuthentication {
}
