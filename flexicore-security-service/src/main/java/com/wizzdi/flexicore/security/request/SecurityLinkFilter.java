package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;
import com.flexicore.model.*;
import com.wizzdi.flexicore.security.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(field = "securityLinkGroupIds", targetField = "securityLinkGroups", fieldType = SecurityLinkGroup.class),
        @IdValid(field = "baseclassIds", targetField = "baseclasses", fieldType = Baseclass.class),
        @IdValid(field = "operationIds", targetField = "operations", fieldType = SecurityOperation.class),
        @IdValid(field = "relevantUserIds", targetField = "relevantUsers", fieldType = SecurityUser.class),

})
public class SecurityLinkFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> relevantUserIds = new HashSet<>();
    private Set<String> securityLinkGroupIds = new HashSet<>();
    @JsonIgnore
    private List<SecurityLinkGroup> securityLinkGroups;

    @JsonIgnore
    private List<SecurityUser> relevantUsers;
    @JsonIgnore
    private List<Role> relevantRoles;

    @JsonIgnore
    private List<SecurityTenant> relevantTenants;

    @JsonIgnore
    private List<Baseclass> baseclasses;

    private Set<String> baseclassIds=new HashSet<>();
    @JsonIgnore
    private List<SecurityOperation> operations;
    private Set<String> operationIds=new HashSet<>();
    private Set<IOperation.Access> accesses;

    private List<SecurityLinkOrder> sorting;

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

    public Set<String> getRelevantUserIds() {
        return relevantUserIds;
    }

    public <T extends SecurityLinkFilter> T setRelevantUserIds(Set<String> relevantUserIds) {
        this.relevantUserIds = relevantUserIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityUser> getRelevantUsers() {
        return relevantUsers;
    }

    public <T extends SecurityLinkFilter> T setRelevantUsers(List<SecurityUser> relevantUsers) {
        this.relevantUsers = relevantUsers;
        return (T) this;
    }

    @JsonIgnore
    public List<Role> getRelevantRoles() {
        return relevantRoles;
    }

    public <T extends SecurityLinkFilter> T setRelevantRoles(List<Role> relevantRoles) {
        this.relevantRoles = relevantRoles;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityTenant> getRelevantTenants() {
        return relevantTenants;
    }

    public <T extends SecurityLinkFilter> T setRelevantTenants(List<SecurityTenant> relevantTenants) {
        this.relevantTenants = relevantTenants;
        return (T) this;
    }

    public Set<String> getSecurityLinkGroupIds() {
        return securityLinkGroupIds;
    }

    public <T extends SecurityLinkFilter> T setSecurityLinkGroupIds(Set<String> securityLinkGroupIds) {
        this.securityLinkGroupIds = securityLinkGroupIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityLinkGroup> getSecurityLinkGroups() {
        return securityLinkGroups;
    }

    public <T extends SecurityLinkFilter> T setSecurityLinkGroups(List<SecurityLinkGroup> securityLinkGroups) {
        this.securityLinkGroups = securityLinkGroups;
        return (T) this;
    }

    public List<SecurityLinkOrder> getSorting() {
        return sorting;
    }

    public <T extends SecurityLinkFilter> T setSorting(List<SecurityLinkOrder> sorting) {
        this.sorting = sorting;
        return (T) this;
    }
}
