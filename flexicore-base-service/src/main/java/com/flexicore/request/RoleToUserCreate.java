package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Role;
import com.flexicore.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Used to create a RoleToUser connection")
public class RoleToUserCreate extends BaseclassCreate{
    private String userId;
    @JsonIgnore
    private User user;
    private String roleId;
    @JsonIgnore
    private Role role;

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public RoleToUserCreate setUser(User user) {
        this.user = user;
        return this;
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public RoleToUserCreate setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public <T extends RoleToUserCreate> T setUserId(String userId) {
        this.userId = userId;
        return (T) this;
    }

    public String getRoleId() {
        return roleId;
    }

    public <T extends RoleToUserCreate> T setRoleId(String roleId) {
        this.roleId = roleId;
        return (T) this;
    }
}
