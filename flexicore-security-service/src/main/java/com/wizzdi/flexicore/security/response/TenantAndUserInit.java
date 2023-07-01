package com.wizzdi.flexicore.security.response;

import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.flexicore.model.TenantToUser;

public class TenantAndUserInit {
    private final SecurityTenant defaultTenant;
    private final SecurityUser admin;
    private final TenantToUser tenantToUser;


    public TenantAndUserInit(SecurityUser admin, SecurityTenant defaultTenant, TenantToUser tenantToUser) {
        this.defaultTenant = defaultTenant;
        this.admin = admin;
        this.tenantToUser = tenantToUser;
    }

    public SecurityTenant getDefaultTenant() {
        return defaultTenant;
    }

    public SecurityUser getAdmin() {
        return admin;
    }

    public TenantToUser getTenantToUser() {
        return tenantToUser;
    }
}
