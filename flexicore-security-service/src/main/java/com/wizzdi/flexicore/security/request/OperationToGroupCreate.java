package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.OperationGroup;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.OperationValid;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "operationGroup", fieldType = OperationGroup.class, field = "operationGroupId")
})
@OperationValid(sourceField = "operationId",targetField = "operation",groups = {Create.class, Update.class})
public class OperationToGroupCreate extends BasicCreate {


    @JsonIgnore
    private SecurityOperation operation;
    @NotNull(groups = Create.class)
    private String operationId;

    @JsonIgnore
    private OperationGroup operationGroup;
    private String operationGroupId;


    @JsonIgnore
    public SecurityOperation getOperation() {
        return operation;
    }

    public <T extends OperationToGroupCreate> T setOperation(SecurityOperation operation) {
        this.operation = operation;
        return (T) this;
    }

    public String getOperationId() {
        return operationId;
    }

    public <T extends OperationToGroupCreate> T setOperationId(String operationId) {
        this.operationId = operationId;
        return (T) this;
    }

    @JsonIgnore
    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public <T extends OperationToGroupCreate> T setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
        return (T) this;
    }

    public String getOperationGroupId() {
        return operationGroupId;
    }

    public <T extends OperationToGroupCreate> T setOperationGroupId(String operationGroupId) {
        this.operationGroupId = operationGroupId;
        return (T) this;
    }
}
