package com.flexicore.request;

import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.model.FilteringInformationHolder;

import javax.persistence.Entity;

@Entity
public class TenantFilter extends FilteringInformationHolder {

    @FieldInfo
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public TenantFilter setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }
}
