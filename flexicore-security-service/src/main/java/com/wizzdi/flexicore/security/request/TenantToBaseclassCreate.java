package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid.List({
        @IdValid(targetField = "tenant", fieldType = SecurityTenant.class, field = "tenantId", groups = {Update.class, Create.class}),
})
public class TenantToBaseclassCreate extends SecurityLinkCreate {

    @JsonIgnore
    private SecurityTenant tenant;
    private String tenantId;


    @JsonIgnore
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends TenantToBaseclassCreate> T setTenant(SecurityTenant baseclass) {
        this.tenant = baseclass;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends TenantToBaseclassCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }
}
