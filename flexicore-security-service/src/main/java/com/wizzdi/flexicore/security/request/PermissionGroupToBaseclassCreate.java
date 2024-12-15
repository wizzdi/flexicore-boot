package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;
import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.security.validation.ClazzValid;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid.List({
        @IdValid(targetField = "permissionGroup", fieldType = PermissionGroup.class, field = "permissionGroupId", groups = {Create.class, Update.class}),


})
public class PermissionGroupToBaseclassCreate extends BasicCreate {

    @JsonIgnore
    private PermissionGroup permissionGroup;
    private String permissionGroupId;

    @JsonAlias("baseclassId")
    private String securedId;
    @ClazzValid
    private Clazz securedType;

    @JsonIgnore
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }

    public String getPermissionGroupId() {
        return permissionGroupId;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setPermissionGroupId(String permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
        return (T) this;
    }

    public String getSecuredId() {
        return securedId;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setSecuredId(String securedId) {
        this.securedId = securedId;
        return (T) this;
    }

    public Clazz getSecuredType() {
        return securedType;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setSecuredType(Clazz securedType) {
        this.securedType = securedType;
        return (T) this;
    }
}
