package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;
import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "permissionGroups", fieldType = PermissionGroup.class, field = "permissionGroupIds", groups = {Create.class, Update.class})


})
public class PermissionGroupToBaseclassMassCreate {

    private List<SecuredHolder> securedHolders = new ArrayList<>();
    @JsonIgnore
    private Set<String> permissionGroupIds = new HashSet<>();
    @JsonIgnore
    private List<PermissionGroup> permissionGroups;

    public List<SecuredHolder> getSecuredHolders() {
        return securedHolders;
    }

    public <T extends PermissionGroupToBaseclassMassCreate> T setSecuredHolders(List<SecuredHolder> securedHolders) {
        this.securedHolders = securedHolders;
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
    public <T extends PermissionGroupToBaseclassMassCreate> T setBaseclasses(List<? extends Baseclass> list){
        this.setSecuredHolders(list.stream().map(f->new SecuredHolder(f.getSecurityId(), Clazz.ofClass(f.getClass()))).toList());
        return (T)this;
    }
}
