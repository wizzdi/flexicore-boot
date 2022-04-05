package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.TenantToBaseClassPremission;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import javax.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "tenantToBaseclassPermission", fieldType = TenantToBaseClassPremission.class, field = "id", groups = {Update.class}),
})
public class TenantToBaseclassPermissionUpdate extends TenantToBaseclassPermissionCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private TenantToBaseClassPremission tenantToBaseclassPermission;

    public String getId() {
        return id;
    }

    public <T extends TenantToBaseclassPermissionUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public TenantToBaseClassPremission getTenantToBaseclassPermission() {
        return tenantToBaseclassPermission;
    }

    public <T extends TenantToBaseclassPermissionUpdate> T setTenantToBaseclassPermission(TenantToBaseClassPremission tenantToBaseclassPermission) {
        this.tenantToBaseclassPermission = tenantToBaseclassPermission;
        return (T) this;
    }
}
