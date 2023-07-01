package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleFilter extends FilteringInformationHolder {

    private Set<String> names;
    private Set<String> usersIds=new HashSet<>();
    @JsonIgnore
    private List<User> users;

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public <T extends RoleFilter> T setUsers(List<User> users) {
        this.users = users;
        return (T) this;
    }

    public Set<String> getUsersIds() {
        return usersIds;
    }

    public <T extends RoleFilter> T setUsersIds(Set<String> usersIds) {
        this.usersIds = usersIds;
        return (T) this;
    }

    public Set<String> getNames() {
        return names;
    }

    public <T extends RoleFilter> T setNames(Set<String> names) {
        this.names = names;
        return (T) this;
    }
}
