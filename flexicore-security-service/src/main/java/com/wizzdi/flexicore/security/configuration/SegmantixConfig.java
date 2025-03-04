package com.wizzdi.flexicore.security.configuration;

import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.model.PermissionGroupToBaseclass_;
import com.flexicore.model.PermissionGroup_;
import com.flexicore.model.SecurityOperation;
import com.flexicore.model.SecurityWildcard;
import com.wizzdi.flexicore.security.service.ClazzService;
import com.wizzdi.segmantix.api.service.FieldPathProvider;
import com.wizzdi.segmantix.api.service.SegmantixCache;
import com.wizzdi.segmantix.service.SecurityRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.Callable;

@Configuration
public class SegmantixConfig {
    @Bean
    public SecurityRepository securityRepository(OperationGroupLinkProviderImpl operationToGroupService, SecurityProviderImpl securityProviderImpl,  Cache dataAccessControlCache, Cache operationToOperationGroupCache, @Lazy SecurityOperation allOps){
        return new SecurityRepository(new FieldPathProviderImpl(), operationToGroupService,securityProviderImpl,new SegmantixCache(new CacheWrapper(dataAccessControlCache),new CacheWrapper(operationToOperationGroupCache)),allOps, SecurityWildcard.class.getSimpleName());
    }

    private static class CacheWrapper implements com.wizzdi.segmantix.api.service.Cache{
        private final Cache cache;

        public CacheWrapper(Cache cache) {
            this.cache = cache;
        }

        @Override
        public <T> T get(Object key, Class<T> type) {
            return this.cache.get(key,type);
        }

        @Override
        public void put(Object key, Object value) {
            this.cache.put(key,value);
}

        @Override
        public void remove(Object key) {
            this.cache.evict(key);
        }

        @Override
        public <T> T get(Object key, Callable<T> valueLoader) {
            return cache.get(key,valueLoader);
        }
    }



    public static class FieldPathProviderImpl implements FieldPathProvider{
        @Override
        public <T> Path<String> getCreatorIdPath(From<?, T> r) {
            if(!Baseclass.class.isAssignableFrom(r.getJavaType())){
               return null;
            }
            return r.get("creator").get("id");
        }

        @Override
        public <T> Path<String> getTenantIdPath(From<?, T> r) {
            if(!Baseclass.class.isAssignableFrom(r.getJavaType())){
                return null;
            }
            return r.get("tenant").get("id");
        }

        @Override
        public <T> Path<String> getTypePath(From<?, T> r) {
            if(isConcreateTable(r.getJavaType())){
                return null;
            }
            try {
                return r.get("dtype");
            } catch (Throwable ignored) {
               return null;
            }
        }

        private <T> boolean isConcreateTable(Class<? extends T> clazz) {
            if(clazz==null){
                return false;
            }

            Class<?> lastConcreate = null;
            for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
                if (!current.isAnnotationPresent(MappedSuperclass.class)&&current.isAnnotationPresent(Entity.class)) {
                    lastConcreate = current;
                } else {
                    break;
                }

            }
            return clazz.equals(lastConcreate);

        }

        @Override
        public <T> Path<String> getSecurityId(From<?, T> r) {
            if(!Baseclass.class.isAssignableFrom(r.getJavaType())){
                return null;
            }
            return r.get("securityId");
        }

        @Override
        public <T> Path<String> getInstanceGroupPath(From<?, T> r, CriteriaBuilder cb) {

            Join<T, PermissionGroupToBaseclass> links = r.join(PermissionGroupToBaseclass_.relatedPermissionGroups.getName(), JoinType.LEFT);
            return links.get(PermissionGroupToBaseclass_.permissionGroup).get(PermissionGroup_.id);
        }
    }

}
