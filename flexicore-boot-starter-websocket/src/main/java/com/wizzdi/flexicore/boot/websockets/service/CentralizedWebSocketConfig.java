package com.wizzdi.flexicore.boot.websockets.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
public class CentralizedWebSocketConfig extends WebSocketConfigurationSupport {
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

}

