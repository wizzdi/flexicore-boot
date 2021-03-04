package com.wizzdi.flexicore.boot.jpa.annotations;

import com.wizzdi.flexicore.boot.jpa.init.FlexiCoreJPAConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(FlexiCoreJPAConfiguration.class)
@Configuration
public @interface EnableFlexiCoreJPAPlugins {
}
