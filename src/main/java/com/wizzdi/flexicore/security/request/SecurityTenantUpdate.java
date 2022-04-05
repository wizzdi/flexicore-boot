package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import javax.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "tenantToUpdate", fieldType = SecurityTenant.class, field = "id", groups = {Update.class}),
})
public class SecurityTenantUpdate extends SecurityTenantCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityTenant tenantToUpdate;

    public String getId() {
        return id;
    }

    public <T extends SecurityTenantUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityTenant getTenantToUpdate() {
        return tenantToUpdate;
    }

    public <T extends SecurityTenantUpdate> T setTenantToUpdate(SecurityTenant tenantToUpdate) {
        this.tenantToUpdate = tenantToUpdate;
        return (T) this;
    }
}
