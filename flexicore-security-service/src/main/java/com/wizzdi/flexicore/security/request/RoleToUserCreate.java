package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

@IdValid.List({
        @IdValid(targetField = "role", fieldType = Role.class, field = "roleId", groups = {Create.class, Update.class}),
        @IdValid(targetField = "securityUser", fieldType = SecurityUser.class, field = "securityUserId", groups = {Create.class, Update.class})


})
public class RoleToUserCreate extends BaselinkCreate {
    @JsonIgnore
    private Role role;
    private String roleId;
    @JsonIgnore
    private SecurityUser securityUser;
    private String securityUserId;

    public RoleToUserCreate(RoleToUserCreate other) {
        super(other);
        this.role = other.role;
        this.roleId = other.roleId;
        this.securityUser = other.securityUser;
        this.securityUserId = other.securityUserId;
    }

    public RoleToUserCreate() {
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public <T extends RoleToUserCreate> T setRole(Role role) {
        this.role = role;
        return (T) this;
    }

    public String getRoleId() {
        return roleId;
    }

    public <T extends RoleToUserCreate> T setRoleId(String roleId) {
        this.roleId = roleId;
        return (T) this;
    }

    @JsonIgnore
    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public <T extends RoleToUserCreate> T setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
        return (T) this;
    }

    public String getSecurityUserId() {
        return securityUserId;
    }

    public <T extends RoleToUserCreate> T setSecurityUserId(String securityUserId) {
        this.securityUserId = securityUserId;
        return (T) this;
    }
}
