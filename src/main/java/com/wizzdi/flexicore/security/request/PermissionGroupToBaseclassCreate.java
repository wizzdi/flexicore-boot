package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid.List({
        @IdValid(targetField = "permissionGroup", fieldType = PermissionGroup.class, field = "permissionGroupId", groups = {Create.class, Update.class}),
        @IdValid(targetField = "baseclass", fieldType = Baseclass.class, field = "baseclassId", groups = {Create.class, Update.class})


})
public class PermissionGroupToBaseclassCreate extends BaselinkCreate {

    @JsonIgnore
    private PermissionGroup permissionGroup;
    private String permissionGroupId;
    @JsonIgnore
    private Baseclass baseclass;
    private String baseclassId;

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

    @JsonIgnore
    public Baseclass getBaseclass() {
        return baseclass;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setBaseclass(Baseclass baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    public String getBaseclassId() {
        return baseclassId;
    }

    public <T extends PermissionGroupToBaseclassCreate> T setBaseclassId(String baseclassId) {
        this.baseclassId = baseclassId;
        return (T) this;
    }
}
