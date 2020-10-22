package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;
import com.flexicore.model.dynamic.DynamicInvoker;

public class CreateOperationRequest {

    private String name;
    private String description;
    private IOperation.Access access;
    private String invokerId;
    @JsonIgnore
    private DynamicInvoker dynamicInvoker;
    private String id;
    private Boolean auditable;

    public String getName() {
        return name;
    }

    public CreateOperationRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateOperationRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getInvokerId() {
        return invokerId;
    }

    public CreateOperationRequest setInvokerId(String invokerId) {
        this.invokerId = invokerId;
        return this;
    }

    @JsonIgnore
    public DynamicInvoker getDynamicInvoker() {
        return dynamicInvoker;
    }

    public CreateOperationRequest setDynamicInvoker(DynamicInvoker dynamicInvoker) {
        this.dynamicInvoker = dynamicInvoker;
        return this;
    }

    public String getId() {
        return id;
    }

    public CreateOperationRequest setId(String id) {
        this.id = id;
        return this;
    }

    public IOperation.Access getAccess() {
        return access;
    }

    public CreateOperationRequest setAccess(IOperation.Access access) {
        this.access = access;
        return this;
    }

    public Boolean isAuditable() {
        return auditable;
    }

    public CreateOperationRequest setAuditable(Boolean auditable) {
        this.auditable = auditable;
        return this;
    }



}
