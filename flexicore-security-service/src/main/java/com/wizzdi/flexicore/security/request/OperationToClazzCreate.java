package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Clazz;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.validation.ClazzValid;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid.List({
        @IdValid(targetField = "securityOperation", fieldType = SecurityOperation.class, field = "securityOperationId", groups = {Create.class, Update.class}),

})
public class OperationToClazzCreate extends BasicCreate {

    @JsonIgnore
    private SecurityOperation securityOperation;
    private String securityOperationId;
    @ClazzValid
    private Clazz type;

    @JsonIgnore
    public SecurityOperation getSecurityOperation() {
        return securityOperation;
    }

    public <T extends OperationToClazzCreate> T setSecurityOperation(SecurityOperation securityOperation) {
        this.securityOperation = securityOperation;
        return (T) this;
    }

    public String getSecurityOperationId() {
        return securityOperationId;
    }

    public <T extends OperationToClazzCreate> T setSecurityOperationId(String securityOperationId) {
        this.securityOperationId = securityOperationId;
        return (T) this;
    }


    public Clazz getType() {
        return type;
    }

    public <T extends OperationToClazzCreate> T setType(Clazz type) {
        this.type = type;
        return (T) this;
    }
}
