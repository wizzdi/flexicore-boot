package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Tenant;

import java.util.HashMap;
import java.util.Map;

public class BaseclassCreate {

    @FieldInfo
    private String name;
    @FieldInfo
    private String description;
    @IdRefFieldInfo(refType = Tenant.class,list = false)
    private String tenantId;
    @JsonIgnore
    private Tenant tenant;
    private Boolean softDelete;

    private final Map<String, Object> other = new HashMap<>();



    public BaseclassCreate() {
    }

    public BaseclassCreate(Baseclass other) {
        this.name = other.getName();
        this.description = other.getDescription();
        this.tenantId = other.getTenant()!=null?other.getTenant().getId():null;
        this.tenant = (Tenant) other.getTenant();
        this.softDelete=other.isSoftDelete();
    }

    public String getName() {
        return name;
    }

    public <T extends BaseclassCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends BaseclassCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public <T extends BaseclassCreate> T setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return (T) this;
    }

    @JsonIgnore
    public Tenant getTenant() {
        return tenant;
    }

    public <T extends BaseclassCreate> T setTenant(Tenant tenant) {
        this.tenant = tenant;
        return (T) this;
    }

    public Boolean getSoftDelete() {
        return softDelete;
    }

    public <T extends BaseclassCreate> T setSoftDelete(Boolean softDelete) {
        this.softDelete = softDelete;
        return (T) this;
    }

    @JsonIgnore
    public boolean supportingDynamic(){
        return false;
    }

    @JsonAnyGetter
    public Map<String, Object> any() {
        return other;
    }

    @JsonAnySetter
    public void set(final String name, final Object value) {
        other.put(name, value);
    }
}
