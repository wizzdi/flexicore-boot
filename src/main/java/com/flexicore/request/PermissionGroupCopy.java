package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.data.jsoncontainers.CreatePermissionGroupRequest;
import com.flexicore.model.PermissionGroup;

public class PermissionGroupCopy extends CreatePermissionGroupRequest {

    private String permissionGroupToCopyId;
    @JsonIgnore
    private PermissionGroup permissionGroup;


    public String getPermissionGroupToCopyId() {
        return permissionGroupToCopyId;
    }

    public <T extends PermissionGroupCopy> T setPermissionGroupToCopyId(String permissionGroupToCopyId) {
        this.permissionGroupToCopyId = permissionGroupToCopyId;
        return (T) this;
    }

    @JsonIgnore
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends PermissionGroupCopy> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }
}
