package com.wizzdi.flexicore.boot.health.annotations;

import com.wizzdi.flexicore.boot.health.init.HealthPluginHandlerModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(HealthPluginHandlerModule.class)
@Configuration
public @interface EnableFlexiCoreHealthPlugins {
}
