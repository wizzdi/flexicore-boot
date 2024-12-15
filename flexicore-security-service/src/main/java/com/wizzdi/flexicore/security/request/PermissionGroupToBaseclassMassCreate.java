package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "permissionGroups", fieldType = PermissionGroup.class, field = "permissionGroupIds", groups = {Create.class, Update.class})


})
public class PermissionGroupToBaseclassMassCreate {

    @JsonAlias("baseclassIds")
    private Set<String> securedIds = new HashSet<>();
    @JsonIgnore
    private Set<String> permissionGroupIds = new HashSet<>();
    @JsonIgnore
    private List<PermissionGroup> permissionGroups;

    public Set<String> getSecuredIds() {
        return securedIds;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setSecuredIds(Set<String> securedIds) {
        this.securedIds = securedIds;
        return (T) this;
    }



    public Set<String> getPermissionGroupIds() {
        return permissionGroupIds;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setPermissionGroupIds(Set<String> permissionGroupIds) {
        this.permissionGroupIds = permissionGroupIds;
        return (T) this;
    }

    @JsonIgnore
    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
        return (T) this;
    }
}
