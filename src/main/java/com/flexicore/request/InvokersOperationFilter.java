package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.dynamic.DynamicInvoker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvokersOperationFilter extends FilteringInformationHolder {

    private Set<String> invokerIds=new HashSet<>();
    @JsonIgnore
    private List<DynamicInvoker> invokers;

    public Set<String> getInvokerIds() {
        return invokerIds;
    }

    public InvokersOperationFilter setInvokerIds(Set<String> invokerIds) {
        this.invokerIds = invokerIds;
        return this;
    }

    @JsonIgnore

    public List<DynamicInvoker> getInvokers() {
        return invokers;
    }

    public InvokersOperationFilter setInvokers(List<DynamicInvoker> invokers) {
        this.invokers = invokers;
        return this;
    }
}
