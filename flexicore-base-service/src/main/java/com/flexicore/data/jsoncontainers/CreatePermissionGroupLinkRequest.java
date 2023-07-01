package com.flexicore.data.jsoncontainers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreatePermissionGroupLinkRequest {

    private Set<String> groupsIds=new HashSet<>();
    @JsonIgnore
    private List<PermissionGroup> permissionGroups;
    private Set<String> baseclassIds=new HashSet<>();
    @JsonIgnore
    private List<Baseclass> baseclasses;



    public Set<String> getBaseclassIds() {
        return baseclassIds;
    }

    public CreatePermissionGroupLinkRequest setBaseclassIds(Set<String> baseclassIds) {
        this.baseclassIds = baseclassIds;
        return this;
    }

    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public CreatePermissionGroupLinkRequest setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return this;
    }

    public Set<String> getGroupsIds() {
        return groupsIds;
    }

    public <T extends CreatePermissionGroupLinkRequest> T setGroupsIds(Set<String> groupsIds) {
        this.groupsIds = groupsIds;
        return (T) this;
    }

    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public <T extends CreatePermissionGroupLinkRequest> T setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
        return (T) this;
    }
}
