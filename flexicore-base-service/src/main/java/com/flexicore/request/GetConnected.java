package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;

public class GetConnected extends FilteringInformationHolder {
    private String wantedClassName;
    @JsonIgnore
    private Class<?> wantedClass;
    private BaselinkFilter baselinkFilter;

    public BaselinkFilter getBaselinkFilter() {
        return baselinkFilter;
    }

    public <T extends GetConnected> T setBaselinkFilter(BaselinkFilter baselinkFilter) {
        this.baselinkFilter = baselinkFilter;
        return (T) this;
    }

    public String getWantedClassName() {
        return wantedClassName;
    }

    public <T extends GetConnected> T setWantedClassName(String wantedClassName) {
        this.wantedClassName = wantedClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<?> getWantedClass() {
        return wantedClass;
    }

    public <T extends GetConnected> T setWantedClass(Class<?> wantedClass) {
        this.wantedClass = wantedClass;
        return (T) this;
    }
}
