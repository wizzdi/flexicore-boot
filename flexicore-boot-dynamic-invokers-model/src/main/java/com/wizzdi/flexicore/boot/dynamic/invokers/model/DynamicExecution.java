package com.wizzdi.flexicore.boot.dynamic.invokers.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.flexicore.model.Baseclass;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DynamicExecution extends Baseclass {


    public DynamicExecution() {
    }

    @OneToMany(targetEntity = ServiceCanonicalName.class,mappedBy = "dynamicExecution",fetch = FetchType.EAGER)

    private List<ServiceCanonicalName> serviceCanonicalNames=new ArrayList<>();
    private String methodName;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
    @JsonTypeIdResolver(PluginLoaderIdResolver.class)
    @Convert(converter = InvokerBodyConverter.class)
    @Lob
    private Object executionParametersHolder;
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastExecuted;
    private String category;


    @OneToMany(targetEntity = ServiceCanonicalName.class,mappedBy = "dynamicExecution",fetch = FetchType.EAGER)
    public List<ServiceCanonicalName> getServiceCanonicalNames() {
        return serviceCanonicalNames;
    }


    public String getMethodName() {
        return methodName;
    }


    public <T extends DynamicExecution> T setServiceCanonicalNames(List<ServiceCanonicalName> serviceCanonicalNames) {
        this.serviceCanonicalNames = serviceCanonicalNames;
        return (T) this;
    }

    public <T extends DynamicExecution> T setMethodName(String methodName) {
        this.methodName = methodName;
        return (T) this;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
    @JsonTypeIdResolver(PluginLoaderIdResolver.class)
    @Convert(converter = InvokerBodyConverter.class)
    @Lob
    public Object getExecutionParametersHolder() {
        return executionParametersHolder;
    }

    public <T extends DynamicExecution> T setExecutionParametersHolder(Object body) {
        this.executionParametersHolder = body;
        return (T) this;
    }

    public OffsetDateTime getLastExecuted() {
        return lastExecuted;
    }

    public <T extends DynamicExecution> T setLastExecuted(OffsetDateTime lastExecuted) {
        this.lastExecuted = lastExecuted;
        return (T) this;
    }

    public String getCategory() {
        return category;
    }

    public <T extends DynamicExecution> T setCategory(String category) {
        this.category = category;
        return (T) this;
    }

    @Override
    public String toString() {
        return "DynamicExecution{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", methodName='" + methodName + '\'' +
                ", category='" + category + '\'' +

                ", body='" + executionParametersHolder + '\'' +
                ", lastExecuted=" + lastExecuted +
                '}';
    }
}
