package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Role;
import com.wizzdi.flexicore.security.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "roles", field = "roleIds",fieldType = Role.class)
})
public class RoleToBaseclassFilter extends SecurityLinkFilter {

    @JsonIgnore
    private List<Role> roles;
    private Set<String> roleIds=new HashSet<>();

    @JsonIgnore
    public List<Role> getRoles() {
        return roles;
    }

    public <T extends RoleToBaseclassFilter> T setRoles(List<Role> roles) {
        this.roles = roles;
        return (T) this;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }

    public <T extends RoleToBaseclassFilter> T setRoleIds(Set<String> roleIds) {
        this.roleIds = roleIds;
        return (T) this;
    }
}
