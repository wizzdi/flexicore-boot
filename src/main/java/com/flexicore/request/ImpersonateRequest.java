package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Tenant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Schema(description = "used with the impersonate service")
public class ImpersonateRequest {

    private String creationTenantId;
    @JsonIgnore
    private Tenant creationTenant;

    private Set<String> readTenantsIds=new HashSet<>();
    @JsonIgnore
    private List<Tenant> readTenants;


    @Schema(description = "tenant to create objects in")
    public String getCreationTenantId() {
        return creationTenantId;
    }

    public <T extends ImpersonateRequest> T setCreationTenantId(String creationTenantId) {
        this.creationTenantId = creationTenantId;
        return (T) this;
    }

    @JsonIgnore
    public Tenant getCreationTenant() {
        return creationTenant;
    }

    public <T extends ImpersonateRequest> T setCreationTenant(Tenant creationTenant) {
        this.creationTenant = creationTenant;
        return (T) this;
    }

    @Schema(description = "limit result set to the given tenants")
    public Set<String> getReadTenantsIds() {
        return readTenantsIds;
    }

    public <T extends ImpersonateRequest> T setReadTenantsIds(Set<String> readTenantsIds) {
        this.readTenantsIds = readTenantsIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Tenant> getReadTenants() {
        return readTenants;
    }

    public <T extends ImpersonateRequest> T setReadTenants(List<Tenant> readTenants) {
        this.readTenants = readTenants;
        return (T) this;
    }
}
