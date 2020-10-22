package com.flexicore.events;

import com.flexicore.model.Role;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;

public class TenantCreatedEvent {
    private final Tenant tenant;
    private final User tenantAdminUser;
    private final Role tenantAdminRole;


    public TenantCreatedEvent(Tenant tenant, Role tenantAdminRole, User tenantAdminUser) {
        this.tenant = tenant;
        this.tenantAdminRole = tenantAdminRole;
        this.tenantAdminUser = tenantAdminUser;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Role getTenantAdminRole() {
        return tenantAdminRole;
    }

    public User getTenantAdminUser() {
        return tenantAdminUser;
    }


}
