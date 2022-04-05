package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Role;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import javax.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "role", fieldType = Role.class, field = "id", groups = {Update.class}),
})
public class RoleUpdate extends RoleCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private Role role;

    public String getId() {
        return id;
    }

    public <T extends RoleUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public <T extends RoleUpdate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }
}
