package com.wizzdi.flexicore.boot.rest.annotations;

import com.wizzdi.flexicore.boot.rest.init.RESTPluginHandlerModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(RESTPluginHandlerModule.class)
@Configuration
public @interface EnableFlexiCoreRESTPlugins {
}
