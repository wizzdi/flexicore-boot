package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionSummaryRequest {

    private Set<String> userIds=new HashSet<>();
    @JsonIgnore
    private List<User> users;
    private Set<String> baseclassIds=new HashSet<>();
    @JsonIgnore
    private List<Baseclass> baseclasses;

    public Set<String> getUserIds() {
        return userIds;
    }

    public <T extends PermissionSummaryRequest> T setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return (T) this;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public <T extends PermissionSummaryRequest> T setUsers(List<User> users) {
        this.users = users;
        return (T) this;
    }

    public Set<String> getBaseclassIds() {
        return baseclassIds;
    }

    public <T extends PermissionSummaryRequest> T setBaseclassIds(Set<String> baseclassIds) {
        this.baseclassIds = baseclassIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends PermissionSummaryRequest> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }
}
