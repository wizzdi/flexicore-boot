package com.flexicore.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${flexicore.login.loginAttemptsMs:600000}")
    private long loginAttemptsMs;
    @Value("${flexicore.users.cache.size:10}")
    private int usersMaxCacheSize;

    @Bean
    public CacheManager loginAttemptsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(loginAttemptsMs, TimeUnit.MILLISECONDS));
        return cacheManager;
    }

    @Bean
    public CacheManager loggedUsersCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(6, TimeUnit.MINUTES).maximumSize(usersMaxCacheSize));
        return cacheManager;
    }

    @Bean
    public CacheManager blacklistedTokensCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(6, TimeUnit.HOURS).maximumSize(10000));
        return cacheManager;
    }

    @Bean
    public CacheManager swaggerCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(5));
        return cacheManager;
    }
    @Bean
    public CacheManager operationCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(50));
        return cacheManager;
    }

    @Bean
    public CacheManager jobCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(1000));
        return cacheManager;
    }

    @Bean
    public CacheManager startedJobsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(1000));
        return cacheManager;
    }

    @Bean
    public CacheManager accessControlCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(100).expireAfterWrite(15, TimeUnit.MINUTES));
        return cacheManager;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "defaultCacheManager")
    public CacheManager defaultCacheManager() {
        return new NoOpCacheManager();
    }
}
