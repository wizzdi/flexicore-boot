package com.wizzdi.flexicore.security.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
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
    @Value("${wizzdi.cache.default.operationAccessControl.size:100}")
    private int operationAccessControlSize;

    @Value("${wizzdi.cache.default.dataAccessControl.size:100}")
    private int dataAccessControlSize;

    @Value("${wizzdi.cache.default.operationToOperationGroup.size:100}")
    private int operationToOperationGroupSize;

    @Bean
    @Qualifier("operationAccessControlCache")
    @ConditionalOnMissingBean(name = "operationAccessControlCacheManager")
    public CacheManager operationAccessControlCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(operationAccessControlSize).expireAfterWrite(15, TimeUnit.MINUTES));
        return cacheManager;
    }

    @Bean
    @Qualifier("dataAccessControlCacheManager")
    @ConditionalOnMissingBean(name = "dataAccessControlCacheManager")
    public CacheManager dataAccessControlCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(dataAccessControlSize).expireAfterWrite(15, TimeUnit.MINUTES));
        return cacheManager;
    }

    @Bean
    @Qualifier("operationToOperationGroupCache")
    @ConditionalOnMissingBean(name = "operationToOperationGroupCacheManager")
    public CacheManager operationToOperationGroupCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(operationToOperationGroupSize));
        return cacheManager;
    }


    @Bean
    @Qualifier("dataAccessControlCache")
    public Cache dataAccessControlCache(@Qualifier("dataAccessControlCacheManager")CacheManager dataAccessControlCacheManager) {
        return dataAccessControlCacheManager.getCache("dataAccessControlCache");
    }

    @Bean
    @Qualifier("operationToOperationGroupCache")
    public Cache operationToOperationGroupCache(@Qualifier("operationToOperationGroupCacheManager")CacheManager operationToOperationGroupCacheManager) {
        return operationToOperationGroupCacheManager.getCache("operationToOperationGroupCache");
    }
    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "defaultCacheManager")
    public CacheManager defaultCacheManager() {
        return new NoOpCacheManager();
    }
}
