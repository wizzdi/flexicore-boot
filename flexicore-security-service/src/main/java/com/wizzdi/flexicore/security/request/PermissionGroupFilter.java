package com.wizzdi.flexicore.security.request;

import java.util.Set;

public class PermissionGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    private Set<String> externalIds;

    public Set<String> getExternalIds() {
        return externalIds;
    }

    public <T extends PermissionGroupFilter> T setExternalIds(Set<String> externalIds) {
        this.externalIds = externalIds;
        return (T) this;
    }

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends PermissionGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }
}
