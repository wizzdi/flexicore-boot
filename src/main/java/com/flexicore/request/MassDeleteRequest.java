package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MassDeleteRequest {

    private Set<String> ids=new HashSet<>();
    @JsonIgnore
    private List<Baseclass> baseclass=new ArrayList<>();

    public Set<String> getIds() {
        return ids;
    }

    public <T extends MassDeleteRequest> T setIds(Set<String> ids) {
        this.ids = ids;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclass() {
        return baseclass;
    }

    public <T extends MassDeleteRequest> T setBaseclass(List<Baseclass> baseclass) {
        this.baseclass = baseclass;
        return (T) this;
    }
}
