package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.TypeRetention;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "tenants", fieldType = SecurityTenant.class, field = "tenantIds", groups = {Create.class, Update.class})
})
public class RoleFilter extends SecurityEntityFilter {

    private Set<String> tenantIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(SecurityTenant.class)
    private List<SecurityTenant> tenants;

    public Set<String> getTenantIds() {
        return tenantIds;
    }

    public <T extends RoleFilter> T setTenantIds(Set<String> tenantIds) {
        this.tenantIds = tenantIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityTenant> getTenants() {
        return tenants;
    }

    public <T extends RoleFilter> T setTenants(List<SecurityTenant> tenants) {
        this.tenants = tenants;
        return (T) this;
    }
}
