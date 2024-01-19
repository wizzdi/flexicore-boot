package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.OperationToGroup;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "operation", fieldType = OperationToGroup.class, field = "id", groups = {Update.class}),
})
public class OperationToGroupUpdate extends OperationToGroupCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private OperationToGroup operationToGroup;

    public String getId() {
        return id;
    }

    public <T extends OperationToGroupUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public OperationToGroup getOperationToGroup() {
        return operationToGroup;
    }

    public <T extends OperationToGroupUpdate> T setOperationToGroup(OperationToGroup operationToGroup) {
        this.operationToGroup = operationToGroup;
        return (T) this;
    }
}
