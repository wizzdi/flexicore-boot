package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;
import com.flexicore.model.Operation;
import com.flexicore.model.dynamic.DynamicInvoker;

public class UpdateOperationRequest extends CreateOperationRequest{

    @JsonIgnore
    private Operation operation;

    @JsonIgnore
    public Operation getOperation() {
        return operation;
    }

    public UpdateOperationRequest setOperation(Operation operation) {
        this.operation = operation;
        return this;
    }

    @Override
    public UpdateOperationRequest setName(String name) {
        return (UpdateOperationRequest) super.setName(name);
    }

    @Override
    public UpdateOperationRequest setDescription(String description) {
        return (UpdateOperationRequest)super.setDescription(description);
    }

    @Override
    public UpdateOperationRequest setInvokerId(String invokerId) {
        return (UpdateOperationRequest)super.setInvokerId(invokerId);
    }

    @Override
    public UpdateOperationRequest setDynamicInvoker(DynamicInvoker dynamicInvoker) {
        return (UpdateOperationRequest)super.setDynamicInvoker(dynamicInvoker);
    }

    @Override
    public UpdateOperationRequest setId(String id) {
        return (UpdateOperationRequest)super.setId(id);
    }

    @Override
    public UpdateOperationRequest setAccess(IOperation.Access access) {
        return (UpdateOperationRequest)super.setAccess(access);
    }

    @Override
    public UpdateOperationRequest setAuditable(Boolean auditable) {
        return (UpdateOperationRequest)super.setAuditable(auditable);
    }

}
