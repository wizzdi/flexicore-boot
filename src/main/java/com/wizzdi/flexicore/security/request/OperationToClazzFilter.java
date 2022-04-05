package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.TypeRetention;
import com.flexicore.model.Clazz;
import com.flexicore.model.SecurityOperation;

import java.util.List;

public class OperationToClazzFilter extends BaselinkFilter {

    @JsonIgnore
    @TypeRetention(SecurityOperation.class)
    private List<SecurityOperation> securityOperations;
    @JsonIgnore
    @TypeRetention(Clazz.class)
    private List<Clazz> clazzes;

    @JsonIgnore
    public List<SecurityOperation> getSecurityOperations() {
        return securityOperations;
    }

    public <T extends OperationToClazzFilter> T setSecurityOperations(List<SecurityOperation> securityOperations) {
        this.securityOperations = securityOperations;
        return (T) this;
    }

    @JsonIgnore
    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends OperationToClazzFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }
}
