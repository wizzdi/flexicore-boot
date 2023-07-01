package com.wizzdi.flexicore.boot.jaxrs.annotations;

import com.wizzdi.flexicore.boot.jaxrs.init.JaxRsPluginHandlerModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(JaxRsPluginHandlerModule.class)
@Configuration
public @interface EnableFlexiCoreJAXRSPlugins {
}
