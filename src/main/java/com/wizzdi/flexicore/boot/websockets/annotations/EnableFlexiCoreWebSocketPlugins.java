package com.wizzdi.flexicore.boot.websockets.annotations;

import com.wizzdi.flexicore.boot.websockets.init.WSPluginHandlerModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(WSPluginHandlerModule.class)
@Configuration
public @interface EnableFlexiCoreWebSocketPlugins {
}
