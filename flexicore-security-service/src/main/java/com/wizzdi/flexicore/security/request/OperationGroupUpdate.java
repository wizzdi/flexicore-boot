package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.OperationGroup;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "operation", fieldType = OperationGroup.class, field = "id", groups = {Update.class}),
})
public class OperationGroupUpdate extends OperationGroupCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private OperationGroup operation;

    public String getId() {
        return id;
    }

    public <T extends OperationGroupUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public OperationGroup getOperation() {
        return operation;
    }

    public <T extends OperationGroupUpdate> T setOperation(OperationGroup operation) {
        this.operation = operation;
        return (T) this;
    }
}
