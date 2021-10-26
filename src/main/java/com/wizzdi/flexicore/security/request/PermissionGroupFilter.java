package com.wizzdi.flexicore.security.request;

import java.util.Set;

public class PermissionGroupFilter extends SecurityEntityFilter {

    private Set<String> externalIds;

    public Set<String> getExternalIds() {
        return externalIds;
    }

    public <T extends PermissionGroupFilter> T setExternalIds(Set<String> externalIds) {
        this.externalIds = externalIds;
        return (T) this;
    }
}
