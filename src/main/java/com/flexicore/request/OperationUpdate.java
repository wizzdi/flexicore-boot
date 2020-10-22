package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.model.Operation;

public class OperationUpdate extends OperationCreate {


    @IdRefFieldInfo(refType = Operation.class,list = false)
    private String id;

    @JsonIgnore
    private Operation operation;

    public String getId() {
        return id;
    }

    public <T extends OperationUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Operation getOperation() {
        return operation;
    }

    public <T extends OperationUpdate> T setOperation(Operation operation) {
        this.operation = operation;
        return (T) this;
    }
}
