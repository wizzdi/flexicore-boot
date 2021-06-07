package com.wizzdi.dynamic.properties.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(DynamicPropertiesModule.class)
@Configuration
public @interface EnableDynamicProperties {
}
