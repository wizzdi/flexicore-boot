package com.wizzdi.flexicore.boot.dynamic.invokers.service.plugin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.flexicore.annotations.TypeRetention;
import com.wizzdi.flexicore.security.request.BaseclassFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestFilter extends BaseclassFilter {

    private Set<String> testEntityIds=new HashSet<>();
    @JsonIgnore
    @TypeRetention(TestEntity.class)
    private List<TestEntity> testEntities;

    public Set<String> getTestEntityIds() {
        return testEntityIds;
    }

    public <T extends TestFilter> T setTestEntityIds(Set<String> testEntityIds) {
        this.testEntityIds = testEntityIds;
        return (T) this;
    }

    @JsonIgnore
    public List<TestEntity> getTestEntities() {
        return testEntities;
    }

    public <T extends TestFilter> T setTestEntities(List<TestEntity> testEntities) {
        this.testEntities = testEntities;
        return (T) this;
    }
}
