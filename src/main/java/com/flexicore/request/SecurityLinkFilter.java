package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecurityLinkFilter extends FilteringInformationHolder {

    private Set<String> leftsidesIds =new HashSet<>();
    @JsonIgnore
    private List<Baseclass> leftsides;

    public Set<String> getLeftsidesIds() {
        return leftsidesIds;
    }

    public <T extends SecurityLinkFilter> T setLeftsidesIds(Set<String> leftsidesIds) {
        this.leftsidesIds = leftsidesIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getLeftsides() {
        return leftsides;
    }

    public <T extends SecurityLinkFilter> T setLeftsides(List<Baseclass> leftsides) {
        this.leftsides = leftsides;
        return (T) this;
    }
}
