package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;

@IdValid.List({
        @IdValid(targetField = "baseclass",field = "baseclassId",fieldType = Baseclass.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "operation",field = "operationId",fieldType = SecurityOperation.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "permissionGroup",field = "permissionGroupId",fieldType = PermissionGroup.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "clazz",field = "clazzId",fieldType = Clazz.class, groups = {Create.class, Update.class})
})
public class SecurityLinkCreate extends BasicCreate {

    @JsonIgnore
    private Baseclass baseclass;

    private String baseclassId;

    @JsonIgnore
    private PermissionGroup permissionGroup;

    private String permissionGroupId;

    @JsonIgnore
    private Clazz clazz;

    private String clazzId;
    @NotEmpty(groups = Create.class)
    private String operationId;
    @JsonIgnore
    private SecurityOperation operation;
    @NotNull(groups = Create.class)
    private IOperation.Access access;

    @JsonIgnore
    public Baseclass getBaseclass() {
        return baseclass;
    }

    public <T extends SecurityLinkCreate> T setBaseclass(Baseclass baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }

    public String getTenantId() {
        return baseclassId;
    }

    public <T extends SecurityLinkCreate> T setBaseclassId(String baseclassId) {
        this.baseclassId = baseclassId;
        return (T) this;
    }

    public String getOperationId() {
        return operationId;
    }

    public <T extends SecurityLinkCreate> T setOperationId(String operationId) {
        this.operationId = operationId;
        return (T) this;
    }

    @JsonIgnore
    public SecurityOperation getOperation() {
        return operation;
    }

    public <T extends SecurityLinkCreate> T setOperation(SecurityOperation operation) {
        this.operation = operation;
        return (T) this;
    }

    public IOperation.Access getAccess() {
        return access;
    }

    public <T extends SecurityLinkCreate> T setAccess(IOperation.Access access) {
        this.access = access;
        return (T) this;
    }

    public String getBaseclassId() {
        return baseclassId;
    }

    @JsonIgnore
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public <T extends SecurityLinkCreate> T setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
        return (T) this;
    }

    public String getPermissionGroupId() {
        return permissionGroupId;
    }

    public <T extends SecurityLinkCreate> T setPermissionGroupId(String permissionGroupId) {
        this.permissionGroupId = permissionGroupId;
        return (T) this;
    }

    @JsonIgnore
    public Clazz getClazz() {
        return clazz;
    }

    public <T extends SecurityLinkCreate> T setClazz(Clazz clazz) {
        this.clazz = clazz;
        return (T) this;
    }

    public String getClazzId() {
        return clazzId;
    }

    public <T extends SecurityLinkCreate> T setClazzId(String clazzId) {
        this.clazzId = clazzId;
        return (T) this;
    }

    @AssertTrue(message = "clazzId or baseclassId or permissionGroupId must be provided",groups = Create.class)
    private boolean isTargetProvided() {
        return !StringUtils.isEmpty(clazzId)||!StringUtils.isEmpty(baseclassId)||!StringUtils.isEmpty(permissionGroupId);
    }
}
