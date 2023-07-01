package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Used to filter result set of TenantToUser")
public class TenantToUserFilter extends FilteringInformationHolder {

    @JsonIgnore
    private List<Tenant> tenants;
    @JsonIgnore
    private List<User> users;

    @JsonIgnore
    public List<Tenant> getTenants() {
        return tenants;
    }

    public <T extends TenantToUserFilter> T setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public <T extends TenantToUserFilter> T setUsers(List<User> users) {
        this.users = users;
        return (T) this;
    }
}
