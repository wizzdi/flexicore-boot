package com.flexicore.request;

import com.flexicore.model.FilteringInformationHolder;

import java.util.HashSet;
import java.util.Set;

public class InvokersFilter extends FilteringInformationHolder {

    private Set<String> invokerTypes=new HashSet<>();
    private Set<Class<?>> classTypes;
    private Set<String> invokerIds=new HashSet<>();

    public Set<String> getInvokerTypes() {
        return invokerTypes;
    }

    public InvokersFilter setInvokerTypes(Set<String> invokerTypes) {
        this.invokerTypes = invokerTypes;
        return this;
    }

    public Set<Class<?>> getClassTypes() {
        return classTypes;
    }

    public InvokersFilter setClassTypes(Set<Class<?>> classTypes) {
        this.classTypes = classTypes;
        return this;
    }

    public Set<String> getInvokerIds() {
        return invokerIds;
    }

    public InvokersFilter setInvokerIds(Set<String> invokerIds) {
        this.invokerIds = invokerIds;
        return this;
    }
}
