package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Role;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid.List({
        @IdValid(targetField = "role", fieldType = Role.class, field = "roleId", groups = {Create.class, Update.class}),

})
public class RoleToBaseclassCreate extends SecurityLinkCreate {

    @JsonIgnore
    private Role role;
    private String roleId;


    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public <T extends RoleToBaseclassCreate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }

    public String getRoleId() {
        return roleId;
    }

    public <T extends RoleToBaseclassCreate> T setRoleId(String roleId) {
        this.roleId = roleId;
        return (T) this;
    }


}
