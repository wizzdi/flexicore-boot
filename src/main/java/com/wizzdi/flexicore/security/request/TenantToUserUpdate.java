package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.TenantToUser;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "tenantToUser", fieldType = TenantToUser.class, field = "id", groups = {Update.class}),
})
public class TenantToUserUpdate extends TenantToUserCreate {

    @NotNull(groups = Update.class)
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
