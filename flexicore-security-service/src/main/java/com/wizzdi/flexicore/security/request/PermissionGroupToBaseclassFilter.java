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
        @IdValid(targetField = "permissionGroups", fieldType = PermissionGroup.class, field = "permissionGroupIds", groups = {Create.class, Update.class}),
        @IdValid(targetField = "baseclasses", fieldType = Baseclass.class, field = "baseclassIds", groups = {Create.class, Update.class})
})
public class PermissionGroupToBaseclassFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @JsonIgnore
    private List<Baseclass> baseclasses;
    private Set<String> baseclassIds=new HashSet<>();
    @JsonIgnore
    private List<PermissionGroup> permissionGroups;

    private Set<String> permissionGroupIds = new HashSet<>();


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    @JsonIgnore
    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
        return (T) this;
    }

    public Set<String> getPermissionGroupIds() {
        return permissionGroupIds;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setPermissionGroupIds(Set<String> permissionGroupIds) {
        this.permissionGroupIds = permissionGroupIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }

    public Set<String> getBaseclassIds() {
        return baseclassIds;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setBaseclassIds(Set<String> baseclassIds) {
        this.baseclassIds = baseclassIds;
        return (T) this;
    }
}
