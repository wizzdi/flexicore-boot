package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.data.jsoncontainers.CreatePermissionGroupRequest;
import com.flexicore.model.PermissionGroup;

public class UpdatePermissionGroup extends CreatePermissionGroupRequest {
    private String id;
    @JsonIgnore
    private PermissionGroup permissionGroup;

    public String getId() {
        return id;
    }

    public UpdatePermissionGroup setId(String id) {
        this.id = id;
        return this;
    }

    @JsonIgnore
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public UpdatePermissionGroup setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return this;
    }
}
