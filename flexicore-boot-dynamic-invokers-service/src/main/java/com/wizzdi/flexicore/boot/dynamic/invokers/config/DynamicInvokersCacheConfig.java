package com.wizzdi.flexicore.boot.dynamic.invokers.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class DynamicInvokersCacheConfig {

    @Bean
    public CacheManager exampleCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(200));
        return cacheManager;
    }
    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "defaultCacheManager")
    public CacheManager defaultCacheManager() {
        return new NoOpCacheManager();
    }
}
