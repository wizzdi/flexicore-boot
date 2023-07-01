package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Operation;
import com.flexicore.model.Role;

public class RoleToBaseclassCreate {

    @JsonIgnore
    private Role role;
    @JsonIgnore

    private Baseclass baseclass;
    @JsonIgnore

    private Operation operation;

    @JsonIgnore

    public Role getRole() {
        return role;
    }

    public RoleToBaseclassCreate setRole(Role role) {
        this.role = role;
        return this;
    }

    @JsonIgnore

    public Baseclass getBaseclass() {
        return baseclass;
    }

    public RoleToBaseclassCreate setBaseclass(Baseclass baseclass) {
        this.baseclass = baseclass;
        return this;
    }

    @JsonIgnore

    public Operation getOperation() {
        return operation;
    }

    public RoleToBaseclassCreate setOperation(Operation operation) {
        this.operation = operation;
        return this;
    }
}
