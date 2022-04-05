package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.OperationToClazz;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import javax.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "operationToClazz", fieldType = OperationToClazz.class, field = "id", groups = {Update.class})

})
public class OperationToClazzUpdate extends OperationToClazzCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private OperationToClazz operationToClazz;

    public String getId() {
        return id;
    }

    public <T extends OperationToClazzUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public OperationToClazz getOperationToClazz() {
        return operationToClazz;
    }

    public <T extends OperationToClazzUpdate> T setOperationToClazz(OperationToClazz operationToClazz) {
        this.operationToClazz = operationToClazz;
        return (T) this;
    }
}
