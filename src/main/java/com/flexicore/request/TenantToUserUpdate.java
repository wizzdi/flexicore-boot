package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.TenantToUser;

public class TenantToUserUpdate extends TenantToUserCreate{

    private String id;
    @JsonIgnore
    private TenantToUser tenantToUser;

    public String getId() {
        return id;
    }

    public <T extends TenantToUserUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public TenantToUser getTenantToUser() {
        return tenantToUser;
    }

    public <T extends TenantToUserUpdate> T setTenantToUser(TenantToUser tenantToUser) {
        this.tenantToUser = tenantToUser;
        return (T) this;
    }
}
