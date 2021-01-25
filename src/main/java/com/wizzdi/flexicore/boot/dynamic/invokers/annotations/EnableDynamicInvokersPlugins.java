package com.wizzdi.flexicore.boot.dynamic.invokers.annotations;

import com.wizzdi.flexicore.boot.dynamic.invokers.init.DynamicInvokersPluginHandlerModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(DynamicInvokersPluginHandlerModule.class)
@Configuration
public @interface EnableDynamicInvokersPlugins {
}
