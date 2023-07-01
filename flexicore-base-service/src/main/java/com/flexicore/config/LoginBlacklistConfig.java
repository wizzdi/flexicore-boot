package com.flexicore.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class LoginBlacklistConfig {

    @Value("${flexicore.loginBlacklistRetentionMs:600000}")
    private long loginBlacklistReturntionMs;


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Cache<String, AtomicInteger> loginBlacklistCache(){
        return CacheBuilder.newBuilder().expireAfterWrite(loginBlacklistReturntionMs, TimeUnit.MILLISECONDS).build();
    }
}
