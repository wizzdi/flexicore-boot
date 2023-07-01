package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionGroupsFilter extends FilteringInformationHolder {



    private Set<String> baseclassIds=new HashSet<>();
    @JsonIgnore
    private List<Baseclass> baseclasses;

    private Set<String> externalIds;



    public Set<String> getBaseclassIds() {
        return baseclassIds;
    }

    public <T extends PermissionGroupsFilter> T setBaseclassIds(Set<String> baseclassIds) {
        this.baseclassIds = baseclassIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends PermissionGroupsFilter> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }

    public Set<String> getExternalIds() {
        return externalIds;
    }

    public <T extends PermissionGroupsFilter> T setExternalIds(Set<String> externalIds) {
        this.externalIds = externalIds;
        return (T) this;
    }
}
