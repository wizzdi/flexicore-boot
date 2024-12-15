package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.TypeRetention;
import com.flexicore.model.Clazz;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.validation.ClazzValid;

import java.util.List;

public class OperationToClazzFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @JsonIgnore
    @TypeRetention(SecurityOperation.class)
    private List<SecurityOperation> securityOperations;
    @ClazzValid
    @JsonIgnore
    @TypeRetention(Clazz.class)
    private List<Clazz> clazzes;


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationToClazzFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

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
