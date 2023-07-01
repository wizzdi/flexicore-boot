package com.wizzdi.flexicore.boot.websockets.annotations;

import com.wizzdi.flexicore.boot.websockets.init.WSPluginHandlerModule;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(WSPluginHandlerModule.class)
@Configuration
@AutoConfigureAfter(ServletServerContainerFactoryBean.class)
@ConditionalOnWebApplication
public @interface EnableFlexiCoreWebSocketPlugins {
}
