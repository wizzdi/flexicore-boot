package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.TypeRetention;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "roles", fieldType = Role.class, field = "rolesIds"),
        @IdValid(targetField = "users", fieldType = SecurityUser.class, field = "userIds")
})
public class RoleToUserFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> rolesIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(Role.class)
    private List<Role> roles;
    private Set<String> userIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(SecurityUser.class)
    private List<SecurityUser> users;
    private String roleNameLike;
    private String userSearchStringLike;

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

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends RoleToUserFilter> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityUser> getUsers() {
        return users;
    }

    public <T extends RoleToUserFilter> T setUsers(List<SecurityUser> users) {
        this.users = users;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends RoleToUserFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public String getRoleNameLike() {
        return roleNameLike;
    }

    public <T extends RoleToUserFilter> T setRoleNameLike(String roleNameLike) {
        this.roleNameLike = roleNameLike;
        return (T) this;
    }

    public String getUserSearchStringLike() {
        return userSearchStringLike;
    }

    public <T extends RoleToUserFilter> T setUserSearchStringLike(String userSearchStringLike) {
        this.userSearchStringLike = userSearchStringLike;
        return (T) this;
    }
}
