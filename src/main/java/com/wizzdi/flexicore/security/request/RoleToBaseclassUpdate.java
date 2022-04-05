package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.RoleToBaseclass;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import javax.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "roleToBaseclass", fieldType = RoleToBaseclass.class, field = "id", groups = {Update.class})

})
public class RoleToBaseclassUpdate extends RoleToBaseclassCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private RoleToBaseclass roleToBaseclass;

    public String getId() {
        return id;
    }

    public <T extends RoleToBaseclassUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public RoleToBaseclass getRoleToBaseclass() {
        return roleToBaseclass;
    }

    public <T extends RoleToBaseclassUpdate> T setRoleToBaseclass(RoleToBaseclass roleToBaseclass) {
        this.roleToBaseclass = roleToBaseclass;
        return (T) this;
    }
}
