package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid(field = "tenantId",targetField = "tenant",fieldType = SecurityTenant.class,groups = {Create.class, Update.class})
public class SecurityUserCreate extends SecurityEntityCreate {

    @JsonIgnore
    private SecurityTenant tenant;
    private String tenantId;

    public SecurityUserCreate(SecurityUserCreate other) {
        super(other);
    }


    public SecurityUserCreate() {
    }

    @JsonIgnore
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends SecurityUserCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends SecurityUserCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }
}
