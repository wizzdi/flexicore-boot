package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.dynamic.DynamicExecution;

public class ExecuteDynamicExecution {
    private String dynamicExecutionId;
    @JsonIgnore
    private DynamicExecution dynamicExecution;

    public String getDynamicExecutionId() {
        return dynamicExecutionId;
    }

    public <T extends ExecuteDynamicExecution> T setDynamicExecutionId(String dynamicExecutionId) {
        this.dynamicExecutionId = dynamicExecutionId;
        return (T) this;
    }

    @JsonIgnore
    public DynamicExecution getDynamicExecution() {
        return dynamicExecution;
    }

    public <T extends ExecuteDynamicExecution> T setDynamicExecution(DynamicExecution dynamicExecution) {
        this.dynamicExecution = dynamicExecution;
        return (T) this;
    }


}
