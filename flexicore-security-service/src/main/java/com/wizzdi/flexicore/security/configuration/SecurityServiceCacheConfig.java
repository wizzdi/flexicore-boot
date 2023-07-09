package com.wizzdi.flexicore.security.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
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
public class SecurityServiceCacheConfig {

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
