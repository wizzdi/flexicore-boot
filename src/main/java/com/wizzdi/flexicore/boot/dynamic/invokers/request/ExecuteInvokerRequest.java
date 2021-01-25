package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.wizzdi.flexicore.boot.dynamic.invokers.interfaces.ExecutionContext;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.PluginLoaderIdResolver;

import java.time.OffsetDateTime;
import java.util.Set;

public class ExecuteInvokerRequest {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
    @JsonTypeIdResolver(PluginLoaderIdResolver.class)
    private Object executionParametersHolder;
    private Set<String> invokerNames;
    private String invokerMethodName;
    @JsonIgnore
    private ExecutionContext executionContext;
    private OffsetDateTime lastExecuted;


    public Set<String> getInvokerNames() {
        return invokerNames;
    }

    public ExecuteInvokerRequest setInvokerNames(Set<String> invokerNames) {
        this.invokerNames = invokerNames;
        return this;
    }

    public String getInvokerMethodName() {
        return invokerMethodName;
    }

    public ExecuteInvokerRequest setInvokerMethodName(String invokerMethodName) {
        this.invokerMethodName = invokerMethodName;
        return this;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
    @JsonTypeIdResolver(PluginLoaderIdResolver.class)
    public Object getExecutionParametersHolder() {
        return executionParametersHolder;
    }

    public ExecuteInvokerRequest setExecutionParametersHolder(Object executionParametersHolder) {
        this.executionParametersHolder = executionParametersHolder;
        return this;
    }

    @JsonIgnore
    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    public <T extends ExecuteInvokerRequest> T setExecutionContext(ExecutionContext executionContext) {
        this.executionContext = executionContext;
        return (T) this;
    }

    public OffsetDateTime getLastExecuted() {
        return lastExecuted;
    }

    public <T extends ExecuteInvokerRequest> T setLastExecuted(OffsetDateTime lastExecuted) {
        this.lastExecuted = lastExecuted;
        return (T) this;
    }
}
