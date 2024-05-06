package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid.List({
        @IdValid(targetField = "user", fieldType = SecurityUser.class, field = "userId", groups = {Update.class, Create.class}),
        @IdValid(targetField = "tenant", fieldType = SecurityTenant.class, field = "tenantId", groups = {Update.class, Create.class})
})
public class TenantToUserCreate extends BasicCreate {

    private Boolean defaultTenant;
    @JsonIgnore
    private SecurityUser user;
    private String userId;

    @JsonIgnore
    private SecurityTenant tenant;
    private String tenantId;

    public TenantToUserCreate(TenantToUserCreate other) {
        super(other);
        this.defaultTenant = other.defaultTenant;
        this.user = other.user;
        this.userId = other.userId;
        this.tenant = other.tenant;
        this.tenantId=other.tenantId;
    }

    public TenantToUserCreate() {
    }

    public Boolean getDefaultTenant() {
        return defaultTenant;
    }

    public <T extends TenantToUserCreate> T setDefaultTenant(Boolean defaultTenant) {
        this.defaultTenant = defaultTenant;
        return (T) this;
    }

    @JsonIgnore
    public SecurityUser getUser() {
        return user;
    }

    public <T extends TenantToUserCreate> T setUser(SecurityUser user) {
        this.user = user;
        return (T) this;
    }

    public String getUserId() {
        return userId;
    }

    public <T extends TenantToUserCreate> T setUserId(String userId) {
        this.userId = userId;
        return (T) this;
    }

    @JsonIgnore
    public SecurityTenant getTenant() {
        return tenant;
    }

    public <T extends TenantToUserCreate> T setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends TenantToUserCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }
}
