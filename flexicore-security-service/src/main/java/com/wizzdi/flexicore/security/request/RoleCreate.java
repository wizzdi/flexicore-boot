package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.constraints.NotEmpty;

@IdValid.List({
        @IdValid(targetField = "tenant", fieldType = SecurityTenant.class, field = "tenantId", groups = {Create.class, Update.class})
})
public class RoleCreate extends SecurityEntityCreate {

    @NotEmpty(groups = Create.class)
    private String tenantId;
    @JsonIgnore
    private SecurityTenant tenant;
    @JsonIgnore
    private Boolean superAdmin;

    public RoleCreate(RoleCreate other) {
        super(other);
        this.tenant=other.tenant;
        this.tenantId = other.tenantId;
        this.superAdmin=other.superAdmin;
    }

    public RoleCreate() {
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends RoleCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }

    @JsonIgnore
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends RoleCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    @JsonIgnore
    public Boolean getSuperAdmin() {
        return superAdmin;
    }

    public <T extends RoleCreate> T setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
        return (T) this;
    }
}
