package com.flexicore.request;

import com.flexicore.model.dynamic.ExecutionParametersHolder;

import java.util.Set;

public class CreateDynamicExecution {

    private String name;
    private String description;
    private Set<String> serviceCanonicalNames;
    private String methodName;
    private ExecutionParametersHolder executionParametersHolder;


    public String getName() {
        return name;
    }

    public <T extends CreateDynamicExecution> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends CreateDynamicExecution> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public Set<String> getServiceCanonicalNames() {
        return serviceCanonicalNames;
    }

    public <T extends CreateDynamicExecution> T setServiceCanonicalNames(Set<String> serviceCanonicalNames) {
        this.serviceCanonicalNames = serviceCanonicalNames;
        return (T) this;
    }

    public String getMethodName() {
        return methodName;
    }

    public <T extends CreateDynamicExecution> T setMethodName(String methodName) {
        this.methodName = methodName;
        return (T) this;
    }

    public ExecutionParametersHolder getExecutionParametersHolder() {
        return executionParametersHolder;
    }

    public <T extends CreateDynamicExecution> T setExecutionParametersHolder(ExecutionParametersHolder executionParametersHolder) {
        this.executionParametersHolder = executionParametersHolder;
        return (T) this;
    }
}
