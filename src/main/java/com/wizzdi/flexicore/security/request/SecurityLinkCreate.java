package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;

public class SecurityLinkCreate extends BaselinkCreate {

    private String valueId;
    @JsonIgnore
    private Baseclass value;
    private String simpleValue;

    public String getValueId() {
        return valueId;
    }

    public <T extends SecurityLinkCreate> T setValueId(String valueId) {
        this.valueId = valueId;
        return (T) this;
    }

    @JsonIgnore
    public Baseclass getValue() {
        return value;
    }

    public <T extends SecurityLinkCreate> T setValue(Baseclass value) {
        this.value = value;
        return (T) this;
    }

    public String getSimpleValue() {
        return simpleValue;
    }

    public <T extends SecurityLinkCreate> T setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
        return (T) this;
    }
}
