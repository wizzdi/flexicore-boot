package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;
import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "baseclasses", fieldType = Baseclass.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "operations", fieldType = SecurityOperation.class, groups = {Create.class, Update.class})
})
public class SecurityLinkFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @JsonIgnore
    private List<Baseclass> baseclasses;

    private Set<String> baseclassIds=new HashSet<>();
    @JsonIgnore
    private List<SecurityOperation> operations;
    private Set<String> operationIds=new HashSet<>();
    private Set<IOperation.Access> accesses;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SecurityLinkFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends SecurityLinkFilter> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }

    public Set<String> getBaseclassIds() {
        return baseclassIds;
    }

    public <T extends SecurityLinkFilter> T setBaseclassIds(Set<String> baseclassIds) {
        this.baseclassIds = baseclassIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityOperation> getOperations() {
        return operations;
    }

    public <T extends SecurityLinkFilter> T setOperations(List<SecurityOperation> operations) {
        this.operations = operations;
        return (T) this;
    }

    public Set<String> getOperationIds() {
        return operationIds;
    }

    public <T extends SecurityLinkFilter> T setOperationIds(Set<String> operationIds) {
        this.operationIds = operationIds;
        return (T) this;
    }

    public Set<IOperation.Access> getAccesses() {
        return accesses;
    }

    public <T extends SecurityLinkFilter> T setAccesses(Set<IOperation.Access> accesses) {
        this.accesses = accesses;
        return (T) this;
    }
}
