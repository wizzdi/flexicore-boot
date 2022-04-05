package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.RoleToUser;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import javax.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "roleToUser", fieldType = RoleToUser.class, field = "id", groups = {Update.class}),
})
public class RoleToUserUpdate extends RoleToUserCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private RoleToUser roleToUser;

    public String getId() {
        return id;
    }

    public <T extends RoleToUserUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public RoleToUser getRoleToUser() {
        return roleToUser;
    }

    public <T extends RoleToUserUpdate> T setRoleToUser(RoleToUser roleToUser) {
        this.roleToUser = roleToUser;
        return (T) this;
    }
}
