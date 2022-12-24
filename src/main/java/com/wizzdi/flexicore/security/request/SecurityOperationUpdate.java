package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "operation", fieldType = SecurityOperation.class, field = "id", groups = {Update.class}),
})
public class SecurityOperationUpdate extends SecurityOperationCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityOperation operation;

    public String getId() {
        return id;
    }

    public <T extends SecurityOperationUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityOperation getOperation() {
        return operation;
    }

    public <T extends SecurityOperationUpdate> T setOperation(SecurityOperation operation) {
        this.operation = operation;
        return (T) this;
    }
}
