package com.wizzdi.flexicore.boot.dynamic.invokers.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.flexicore.model.Baseclass;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DynamicExecution {

    @Id
    private String id;
    private String name;
    private String description;
    @JsonIgnore
    @ManyToOne(targetEntity = Baseclass.class)
    private Baseclass security;


    public DynamicExecution() {
    }

    @OneToMany(targetEntity = ServiceCanonicalName.class,mappedBy = "dynamicExecution",fetch = FetchType.EAGER)

    private List<ServiceCanonicalName> serviceCanonicalNames=new ArrayList<>();
    private String methodName;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
    @JsonTypeIdResolver(PluginLoaderIdResolver.class)
    @Convert(converter = InvokerBodyConverter.class)
    private Object body;
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastExecuted;


    @OneToMany(targetEntity = ServiceCanonicalName.class,mappedBy = "dynamicExecution",fetch = FetchType.EAGER)
    public List<ServiceCanonicalName> getServiceCanonicalNames() {
        return serviceCanonicalNames;
    }


    public String getMethodName() {
        return methodName;
    }

    @JsonIgnore
    @ManyToOne(targetEntity = Baseclass.class)
    public Baseclass getSecurity() {
        return security;
    }

    public <T extends DynamicExecution> T setSecurity(Baseclass security) {
        this.security = security;
        return (T) this;
    }

    @Id
    public String getId() {
        return id;
    }

    public <T extends DynamicExecution> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public <T extends DynamicExecution> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends DynamicExecution> T setDescription(String description) {
        this.description = description;
        return (T) this;
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
    public Object getBody() {
        return body;
    }

    public <T extends DynamicExecution> T setBody(Object body) {
        this.body = body;
        return (T) this;
    }

    public OffsetDateTime getLastExecuted() {
        return lastExecuted;
    }

    public <T extends DynamicExecution> T setLastExecuted(OffsetDateTime lastExecuted) {
        this.lastExecuted = lastExecuted;
        return (T) this;
    }

    @Override
    public String toString() {
        return "DynamicExecution{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", serviceCanonicalNames=" + serviceCanonicalNames +
                ", methodName='" + methodName + '\'' +
                ", body='" + body + '\'' +
                ", lastExecuted=" + lastExecuted +
                '}';
    }
}
