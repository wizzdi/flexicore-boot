package com.flexicore.response;

import com.flexicore.model.Tenant;

public class BaseclassCount {
    private Tenant tenant;
    private String canonicalName;
    private long count;

    public BaseclassCount(Tenant tenant, String canonicalName, long count) {
        this(canonicalName,count);
        this.tenant = tenant;
    }

    public BaseclassCount(String canonicalName, long count) {
        this();
        this.canonicalName = canonicalName;
        this.count = count;
    }

    public BaseclassCount() {
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public <T extends BaseclassCount> T setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
        return (T) this;
    }

    public long getCount() {
        return count;
    }

    public <T extends BaseclassCount> T setCount(long count) {
        this.count = count;
        return (T) this;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public <T extends BaseclassCount> T setTenant(Tenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }
}
