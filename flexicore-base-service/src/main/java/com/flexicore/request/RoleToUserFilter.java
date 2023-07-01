package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.Role;
import com.flexicore.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleToUserFilter extends FilteringInformationHolder {


    private Set<String> usersIds=new HashSet<>();
    @JsonIgnore
    private List<User> users;
    private Set<String> rolesIds=new HashSet<>();
    @JsonIgnore
    private List<Role> roles;

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public <T extends RoleToUserFilter> T setUsers(List<User> users) {
        this.users = users;
        return (T) this;
    }

    public Set<String> getUsersIds() {
        return usersIds;
    }

    public <T extends RoleToUserFilter> T setUsersIds(Set<String> usersIds) {
        this.usersIds = usersIds;
        return (T) this;
    }

    public Set<String> getRolesIds() {
        return rolesIds;
    }

    public <T extends RoleToUserFilter> T setRolesIds(Set<String> rolesIds) {
        this.rolesIds = rolesIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Role> getRoles() {
        return roles;
    }

    public <T extends RoleToUserFilter> T setRoles(List<Role> roles) {
        this.roles = roles;
        return (T) this;
    }
}
