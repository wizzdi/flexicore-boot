package com.wizzdi.flexicore.boot.dynamic.invokers.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ServiceCanonicalName {

    @Id
    private String id;

    @ManyToOne(targetEntity = DynamicExecution.class,cascade = CascadeType.MERGE)
    @JsonIgnore
    private DynamicExecution dynamicExecution;

    private String serviceCanonicalName;

    @Id
    public String getId() {
        return id;
    }

    public ServiceCanonicalName setId(String id) {
        this.id = id;
        return this;
    }

    public String getServiceCanonicalName() {
        return serviceCanonicalName;
    }

    public ServiceCanonicalName setServiceCanonicalName(String serviceCanonicalName) {
        this.serviceCanonicalName = serviceCanonicalName;
        return this;
    }

    @ManyToOne(targetEntity = DynamicExecution.class,cascade = CascadeType.MERGE)
    @JsonIgnore
    public DynamicExecution getDynamicExecution() {
        return dynamicExecution;
    }

    public ServiceCanonicalName setDynamicExecution(DynamicExecution dynamicExecution) {
        this.dynamicExecution = dynamicExecution;
        return this;
    }

    @Override
    public String toString() {
        return "ServiceCanonicalName{" +
                "id='" + id + '\'' +
                ", serviceCanonicalName='" + serviceCanonicalName + '\'' +
                '}';
    }
}
