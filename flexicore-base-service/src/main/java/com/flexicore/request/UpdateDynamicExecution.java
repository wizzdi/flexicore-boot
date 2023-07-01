package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;

public class UpdateDynamicExecution extends CreateDynamicExecution{
    private String id;
    @JsonIgnore
    private DynamicExecution dynamicExecution;

    public String getId() {
        return id;
    }

    public <T extends UpdateDynamicExecution> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public DynamicExecution getDynamicExecution() {
        return dynamicExecution;
    }

    public <T extends UpdateDynamicExecution> T setDynamicExecution(DynamicExecution dynamicExecution) {
        this.dynamicExecution = dynamicExecution;
        return (T) this;
    }
}
