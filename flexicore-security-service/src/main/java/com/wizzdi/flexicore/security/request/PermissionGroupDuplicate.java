package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.security.validation.IdValid;

@IdValid(targetField = "permissionGroup",field = "id",fieldType = PermissionGroup.class)
public class PermissionGroupDuplicate {

    private String id;

    @JsonIgnore
    private PermissionGroup permissionGroup;

    public String getId() {
        return id;
    }

    public <T extends PermissionGroupDuplicate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends PermissionGroupDuplicate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }
}
