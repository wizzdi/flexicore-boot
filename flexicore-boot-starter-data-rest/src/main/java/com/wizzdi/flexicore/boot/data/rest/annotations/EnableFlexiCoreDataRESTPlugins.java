package com.wizzdi.flexicore.boot.data.rest.annotations;

import com.wizzdi.flexicore.boot.data.rest.init.DataRESTPluginHandlerModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(DataRESTPluginHandlerModule.class)
@Configuration
public @interface EnableFlexiCoreDataRESTPlugins {
}
