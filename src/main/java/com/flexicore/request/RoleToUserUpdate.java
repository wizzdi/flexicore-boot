package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.RoleToUser;

public class RoleToUserUpdate extends RoleToUserCreate{

    private String id;
    @JsonIgnore
    private RoleToUser roleToUser;

    public String getId() {
        return id;
    }

    public <T extends RoleToUserUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public RoleToUser getRoleToUser() {
        return roleToUser;
    }

    public <T extends RoleToUserUpdate> T setRoleToUser(RoleToUser roleToUser) {
        this.roleToUser = roleToUser;
        return (T) this;
    }
}
