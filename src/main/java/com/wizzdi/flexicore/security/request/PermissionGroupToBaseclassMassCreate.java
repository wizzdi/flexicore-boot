package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "baseclasses", fieldType = Baseclass.class, field = "baseclassesIds", groups = {Create.class, Update.class}),
        @IdValid(targetField = "permissionGroups", fieldType = PermissionGroup.class, field = "permissionGroupIds", groups = {Create.class, Update.class})


})
public class PermissionGroupToBaseclassMassCreate {

    private Set<String> baseclassesIds = new HashSet<>();
    @JsonIgnore
    private List<Baseclass> baseclasses;
    private Set<String> permissionGroupIds = new HashSet<>();
    @JsonIgnore
    private List<PermissionGroup> permissionGroups;

    public Set<String> getBaseclassesIds() {
        return baseclassesIds;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setBaseclassesIds(Set<String> baseclassesIds) {
        this.baseclassesIds = baseclassesIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
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
