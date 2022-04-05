package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import javax.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "permissionGroupToBaseclass", fieldType = PermissionGroupToBaseclass.class, field = "id", groups = {Update.class})
})
public class PermissionGroupToBaseclassUpdate extends PermissionGroupToBaseclassCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private PermissionGroupToBaseclass permissionGroupToBaseclass;

    public String getId() {
        return id;
    }

    public <T extends PermissionGroupToBaseclassUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public PermissionGroupToBaseclass getPermissionGroupToBaseclass() {
        return permissionGroupToBaseclass;
    }

    public <T extends PermissionGroupToBaseclassUpdate> T setPermissionGroupToBaseclass(PermissionGroupToBaseclass permissionGroupToBaseclass) {
        this.permissionGroupToBaseclass = permissionGroupToBaseclass;
        return (T) this;
    }
}
