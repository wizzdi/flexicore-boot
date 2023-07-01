package com.wizzdi.flexicore.boot.base.annotations.plugins;

import com.wizzdi.flexicore.boot.base.init.PluginInit;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(PluginInit.class)
@Configuration
public @interface EnableFlexiCorePlugins {

}
