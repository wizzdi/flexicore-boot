package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.dynamic.ExecutionParametersHolder;

public class DynamicExecutionFilter extends FilteringInformationHolder {

    private String methodNameLike;
    private String serviceCanonicalNameLike;
    private String executionParameterHolderCanonicalName;
    @JsonIgnore
    private Class<? extends ExecutionParametersHolder> executionParameterHolderType;


    public String getMethodNameLike() {
        return methodNameLike;
    }

    public <T extends DynamicExecutionFilter> T setMethodNameLike(String methodNameLike) {
        this.methodNameLike = methodNameLike;
        return (T) this;
    }

    public String getServiceCanonicalNameLike() {
        return serviceCanonicalNameLike;
    }

    public <T extends DynamicExecutionFilter> T setServiceCanonicalNameLike(String serviceCanonicalNameLike) {
        this.serviceCanonicalNameLike = serviceCanonicalNameLike;
        return (T) this;
    }

    public String getExecutionParameterHolderCanonicalName() {
        return executionParameterHolderCanonicalName;
    }

    public <T extends DynamicExecutionFilter> T setExecutionParameterHolderCanonicalName(String executionParameterHolderCanonicalName) {
        this.executionParameterHolderCanonicalName = executionParameterHolderCanonicalName;
        return (T) this;
    }

    @JsonIgnore
    public Class<? extends ExecutionParametersHolder> getExecutionParameterHolderType() {
        return executionParameterHolderType;
    }

    public <T extends DynamicExecutionFilter> T setExecutionParameterHolderType(Class<? extends ExecutionParametersHolder> executionParameterHolderType) {
        this.executionParameterHolderType = executionParameterHolderType;
        return (T) this;
    }
}
