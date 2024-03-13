package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Extension
public class ExampleCacheService implements Plugin {

    @Autowired
    @Lazy
    private ExampleService exampleService;


    @Cacheable(value = "exampleCache", key = "#c.getCanonicalName()", unless = "#result==null",cacheManager = "exampleCacheManager")
    public Object getExampleCached(Class<?> c) {
        return exampleService.generateExample(c);
    }

    @CachePut(value = "exampleCache", key = "#c.getCanonicalName()", unless = "#result==null",cacheManager = "exampleCacheManager")
    public Object updateExampleCache(Class<?> c,Object value){
        return value;
    }

}
