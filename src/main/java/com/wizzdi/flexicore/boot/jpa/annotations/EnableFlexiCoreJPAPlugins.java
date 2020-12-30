package com.wizzdi.flexicore.boot.jpa.annotations;

import com.wizzdi.flexicore.boot.jpa.init.EclipseLinkJpaConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(EclipseLinkJpaConfiguration.class)
@Configuration
public @interface EnableFlexiCoreJPAPlugins {
}
