package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.model.Access;
import com.flexicore.model.*;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;

@IdValid.List({
        @IdValid(targetField = "securityLinkGroup",field = "securityLinkGroupId",fieldType = SecurityLinkGroup.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "operation",field = "operationId",fieldType = SecurityOperation.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "operationGroup",field = "operationGroupId",fieldType = OperationGroup.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "permissionGroup",field = "permissionGroupId",fieldType = PermissionGroup.class, groups = {Create.class, Update.class}),
        @IdValid(targetField = "clazz",field = "clazzId",fieldType = Clazz.class, groups = {Create.class, Update.class})
})
public class SecurityLinkCreate extends BasicCreate {

    private String securityLinkGroupId;
    @JsonIgnore
    private SecurityLinkGroup securityLinkGroup;



    @JsonAlias("baseclassId")
    private String securedId;

    @JsonIgnore
    private PermissionGroup permissionGroup;

    private String permissionGroupId;

    @JsonIgnore
    private Clazz clazz;

    private String clazzId;
    private String operationId;
    @JsonIgnore
    private SecurityOperation operation;

    private String operationGroupId;
    @JsonIgnore
    private OperationGroup operationGroup;

    @NotNull(groups = Create.class)
    private Access access;



    public String getTenantId() {
        return securedId;
    }

    public <T extends SecurityLinkCreate> T setSecuredId(String securedId) {
        this.securedId = securedId;
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

    public Access getAccess() {
        return access;
    }

    public <T extends SecurityLinkCreate> T setAccess(Access access) {
        this.access = access;
        return (T) this;
    }

    public String getSecuredId() {
        return securedId;
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

    public String getOperationGroupId() {
        return operationGroupId;
    }

    public <T extends SecurityLinkCreate> T setOperationGroupId(String operationGroupId) {
        this.operationGroupId = operationGroupId;
        return (T) this;
    }

    @JsonIgnore
    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public <T extends SecurityLinkCreate> T setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
        return (T) this;
    }

    public String getSecurityLinkGroupId() {
        return securityLinkGroupId;
    }

    public <T extends SecurityLinkCreate> T setSecurityLinkGroupId(String securityLinkGroupId) {
        this.securityLinkGroupId = securityLinkGroupId;
        return (T) this;
    }

    @JsonIgnore
    public SecurityLinkGroup getSecurityLinkGroup() {
        return securityLinkGroup;
    }

    public <T extends SecurityLinkCreate> T setSecurityLinkGroup(SecurityLinkGroup securityLinkGroup) {
        this.securityLinkGroup = securityLinkGroup;
        return (T) this;
    }

    @AssertTrue(message = "clazzId or baseclassId or permissionGroupId must be provided",groups = Create.class)
    private boolean isTargetProvided() {
        return !StringUtils.isEmpty(clazzId)||!StringUtils.isEmpty(securedId)||!StringUtils.isEmpty(permissionGroupId);
    }

    @AssertTrue(message = "operationId or operationGroupId must be provided",groups = Create.class)
    private boolean isOperationOrOperationGroupProvided() {
        return !StringUtils.isEmpty(operationId)||!StringUtils.isEmpty(operationGroupId);
    }

}
