package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "used to create a tenant to user connection")
public class TenantToUserCreate extends BaseclassCreate{

    private String userId;
    @JsonIgnore
    private User user;
    private Boolean defaultTenant;

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public TenantToUserCreate setUser(User user) {
        this.user = user;
        return this;
    }


    public Boolean isDefaultTenant() {
        return defaultTenant;
    }

    public TenantToUserCreate setDefaultTenant(Boolean defaultTenant) {
        this.defaultTenant = defaultTenant;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public <T extends TenantToUserCreate> T setUserId(String userId) {
        this.userId = userId;
        return (T) this;
    }
}
