package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.TypeRetention;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "users", fieldType = SecurityUser.class, field = "userIds"),
        @IdValid(targetField = "tenants", fieldType = SecurityTenant.class, field = "tenantsIds"),

})
public class TenantToUserFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> userIds = new HashSet<>();
    @TypeRetention(SecurityUser.class)
    @JsonIgnore
    private List<SecurityUser> users;
    private Set<String> tenantsIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(SecurityTenant.class)
    private List<SecurityTenant> tenants;
    private String userSearchStringLike;
    private String tenantNameLike;

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends TenantToUserFilter> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends TenantToUserFilter> T setUsers(List<SecurityUser> users) {
        this.users = users;
        return (T) this;
    }

    public Set<String> getTenantsIds() {
        return tenantsIds;
    }

    public <T extends TenantToUserFilter> T setTenantsIds(Set<String> tenantsIds) {
        this.tenantsIds = tenantsIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityTenant> getTenants() {
        return tenants;
    }

    public <T extends TenantToUserFilter> T setTenants(List<SecurityTenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends TenantToUserFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public String getUserSearchStringLike() {
        return userSearchStringLike;
    }

    public <T extends TenantToUserFilter> T setUserSearchStringLike(String userSearchStringLike) {
        this.userSearchStringLike = userSearchStringLike;
        return (T) this;
    }

    public String getTenantNameLike() {
        return tenantNameLike;
    }

    public <T extends TenantToUserFilter> T setTenantNameLike(String tenantNameLike) {
        this.tenantNameLike = tenantNameLike;
        return (T) this;
    }
}
