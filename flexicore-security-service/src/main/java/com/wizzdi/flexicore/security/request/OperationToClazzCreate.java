package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Clazz;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.validation.ClazzValid;
public class OperationToClazzCreate extends BasicCreate {

    @JsonIgnore
    private SecurityOperation securityOperation;
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



    public Clazz getType() {
        return type;
    }

    public <T extends OperationToClazzCreate> T setType(Clazz type) {
        this.type = type;
        return (T) this;
    }
}
