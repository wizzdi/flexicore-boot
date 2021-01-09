package com.flexicore.response;

import com.flexicore.model.Role;
import com.flexicore.model.SecurityUser;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "used to return user and default tenant")
public class UserProfile {

    private SecurityUser user;
    //for backwards compatibility
    private Tenant tenant;
    private List<Tenant> tenants;
    private List<Role> roles;


    public SecurityUser getUser() {
        return user;
    }

    public <T extends UserProfile> T setUser(SecurityUser user) {
        this.user = user;
        return (T) this;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public <T extends UserProfile> T setTenant(Tenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    public List<Tenant> getTenants() {
        return tenants;
    }

    public <T extends UserProfile> T setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public <T extends UserProfile> T setRoles(List<Role> roles) {
        this.roles = roles;
        return (T) this;
    }
}
