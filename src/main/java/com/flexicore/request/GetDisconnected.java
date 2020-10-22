package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;

public class GetDisconnected extends FilteringInformationHolder {
    private String wantedClassName;
    @JsonIgnore
    private Class<? extends Baseclass> wantedClass;
    private BaselinkFilter baselinkFilter;

    public BaselinkFilter getBaselinkFilter() {
        return baselinkFilter;
    }

    public <T extends GetDisconnected> T setBaselinkFilter(BaselinkFilter baselinkFilter) {
        this.baselinkFilter = baselinkFilter;
        return (T) this;
    }

    public String getWantedClassName() {
        return wantedClassName;
    }

    public <T extends GetDisconnected> T setWantedClassName(String wantedClassName) {
        this.wantedClassName = wantedClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<? extends Baseclass> getWantedClass() {
        return wantedClass;
    }

    public <T extends GetDisconnected> T setWantedClass(Class<? extends Baseclass> wantedClass) {
        this.wantedClass = wantedClass;
        return (T) this;
    }
}
