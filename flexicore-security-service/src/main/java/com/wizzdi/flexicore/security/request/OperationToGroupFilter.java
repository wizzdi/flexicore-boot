package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.OperationGroup;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.OperationValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "operationGroups", fieldType = OperationGroup.class, field = "operationGroupIds")
})
@OperationValid(targetField = "operations",sourceField = "operationIds")
public class OperationToGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @JsonIgnore
    private List<SecurityOperation> operations;
    private Set<String> operationIds=new HashSet<>();

    @JsonIgnore
    private List<OperationGroup> operationGroups;
    private Set<String> operationGroupIds=new HashSet<>();

    @JsonIgnore
    public List<SecurityOperation> getOperations() {
        return operations;
    }

    public <T extends OperationToGroupFilter> T setOperations(List<SecurityOperation> operations) {
        this.operations = operations;
        return (T) this;
    }

    public Set<String> getOperationIds() {
        return operationIds;
    }

    public <T extends OperationToGroupFilter> T setOperationIds(Set<String> operationIds) {
        this.operationIds = operationIds;
        return (T) this;
    }

    @JsonIgnore
    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public <T extends OperationToGroupFilter> T setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
        return (T) this;
    }

    public Set<String> getOperationGroupIds() {
        return operationGroupIds;
    }

    public <T extends OperationToGroupFilter> T setOperationGroupIds(Set<String> operationGroupIds) {
        this.operationGroupIds = operationGroupIds;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends OperationToGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
