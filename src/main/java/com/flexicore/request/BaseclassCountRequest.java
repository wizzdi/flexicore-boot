package com.flexicore.request;

import com.flexicore.model.FilteringInformationHolder;

public class BaseclassCountRequest extends FilteringInformationHolder {

    private boolean groupByTenant;

    public boolean isGroupByTenant() {
        return groupByTenant;
    }

    public <T extends BaseclassCountRequest> T setGroupByTenant(boolean groupByTenant) {
        this.groupByTenant = groupByTenant;
        return (T) this;
    }
}
