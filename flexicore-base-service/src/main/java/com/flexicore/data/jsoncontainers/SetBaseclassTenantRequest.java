package com.flexicore.data.jsoncontainers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Tenant;

import java.util.List;
import java.util.Set;

public class SetBaseclassTenantRequest {

    private Set<String> ids;
    @JsonIgnore
    private List<Baseclass> baseclasses;
    private String tenantId;
    @JsonIgnore
    private Tenant tenant;

    public Set<String> getIds() {
        return ids;
    }

    public SetBaseclassTenantRequest setIds(Set<String> ids) {
        this.ids = ids;
        return this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public SetBaseclassTenantRequest setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public SetBaseclassTenantRequest setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    @JsonIgnore
    public Tenant getTenant() {
        return tenant;
    }

    public SetBaseclassTenantRequest setTenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }
}
