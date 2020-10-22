package com.flexicore.model.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Operation;
import com.flexicore.security.SecurityContext;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DynamicInvoker extends Baseclass {



    public DynamicInvoker() {
    }

    public DynamicInvoker(String name, SecurityContext securityContext) {
        super(name, securityContext);
    }

    @OneToMany(targetEntity = Operation.class,mappedBy = "dynamicInvoker")
    @JsonIgnore
    private List<Operation> operations=new ArrayList<>();
    private String canonicalName;

    @OneToMany(targetEntity = Operation.class,mappedBy = "dynamicInvoker")
    @JsonIgnore
    public List<Operation> getOperations() {
        return operations;
    }

    public DynamicInvoker setOperations(List<Operation> operations) {
        this.operations = operations;
        return this;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public DynamicInvoker setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
        return this;
    }
}
