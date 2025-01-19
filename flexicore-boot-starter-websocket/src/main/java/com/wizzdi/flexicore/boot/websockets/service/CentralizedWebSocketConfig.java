package com.wizzdi.flexicore.boot.websockets.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.ServletWebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
public class CentralizedWebSocketConfig extends WebSocketConfigurationSupport implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(CentralizedWebSocketConfig.class);

    private final FlexiCorePluginManager pluginManager;

    public CentralizedWebSocketConfig(FlexiCorePluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    protected void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        List<WebSocketConfigurer> configurers = new ArrayList<>(pluginManager.getAllApplicationContexts().stream().map(f -> f.getBeansOfType(WebSocketConfigurer.class)).map(f -> f.values()).flatMap(Collection::stream).collect(Collectors.toMap(f -> ClassUtils.getUserClass(f.getClass()).getCanonicalName(), f -> f, (a, b) -> a)).values());
        logger.info("websockets configurations: {}", configurers.stream().map(f -> f.getClass().getCanonicalName()).collect(Collectors.joining(",")));
        for (WebSocketConfigurer configurer : configurers) {
            configurer.registerWebSocketHandlers(registry);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        validate();
    }

    private void validate() {
        for (ApplicationContext applicationContext : pluginManager.getAllApplicationContexts()) {
            Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableWebSocket.class);
            if (!beansWithAnnotation.isEmpty()) {
                String beans = beansWithAnnotation.values().stream().map(f -> ClassUtils.getUserClass(f.getClass()).getCanonicalName()).collect(Collectors.joining(System.lineSeparator()));
                throw new IllegalStateException(
                "Conflicting WebSocket configurations detected. " +
                "Do not use @EnableWebSocket with @EnableWebSocketPlugins. , Beans using @EnableWebSocket are:\n "+ beans
            );
        }
        }
    }
}

