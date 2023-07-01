package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "securityUser", fieldType = SecurityUser.class, field = "id", groups = {Update.class}),
})
public class SecurityUserUpdate extends SecurityUserCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityUser securityUser;

    public String getId() {
        return id;
    }

    public <T extends SecurityUserUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public <T extends SecurityUserUpdate> T setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
        return (T) this;
    }
}
