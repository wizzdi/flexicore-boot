package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Tenant;

public class TenantUpdate extends TenantCreate {

    private String id;
    @JsonIgnore
    private Tenant tenantToUpdate;

    public String getId() {
        return id;
    }

    public <T extends TenantUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Tenant getTenantToUpdate() {
        return tenantToUpdate;
    }

    public <T extends TenantUpdate> T setTenantToUpdate(Tenant tenant) {
        this.tenantToUpdate = tenant;
        return (T) this;
    }
}
