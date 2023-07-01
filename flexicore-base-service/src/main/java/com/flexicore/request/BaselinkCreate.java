package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baselink;

public class BaselinkCreate {
    private String name;
    private String description;
    private String leftsideId;
    @JsonIgnore
    private Baseclass leftside;
    private String rightsideId;
    @JsonIgnore
    private Baseclass rightside;
    private String valueId;
    @JsonIgnore
    private Baseclass value;
    private String simpleValue;
    private String linkClassName;
    @JsonIgnore
    private Class<? extends Baselink> linkClass;

    public String getName() {
        return name;
    }

    public <T extends BaselinkCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends BaselinkCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getLeftsideId() {
        return leftsideId;
    }

    public <T extends BaselinkCreate> T setLeftsideId(String leftsideId) {
        this.leftsideId = leftsideId;
        return (T) this;
    }

    @JsonIgnore
    public Baseclass getLeftside() {
        return leftside;
    }

    public <T extends BaselinkCreate> T setLeftside(Baseclass leftside) {
        this.leftside = leftside;
        return (T) this;
    }

    public String getRightsideId() {
        return rightsideId;
    }

    public <T extends BaselinkCreate> T setRightsideId(String rightsideId) {
        this.rightsideId = rightsideId;
        return (T) this;
    }

    @JsonIgnore
    public Baseclass getRightside() {
        return rightside;
    }

    public <T extends BaselinkCreate> T setRightside(Baseclass rightside) {
        this.rightside = rightside;
        return (T) this;
    }

    public String getValueId() {
        return valueId;
    }

    public <T extends BaselinkCreate> T setValueId(String valueId) {
        this.valueId = valueId;
        return (T) this;
    }

    @JsonIgnore
    public Baseclass getValue() {
        return value;
    }

    public <T extends BaselinkCreate> T setValue(Baseclass value) {
        this.value = value;
        return (T) this;
    }

    public String getSimpleValue() {
        return simpleValue;
    }

    public <T extends BaselinkCreate> T setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
        return (T) this;
    }

    public String getLinkClassName() {
        return linkClassName;
    }

    public <T extends BaselinkCreate> T setLinkClassName(String linkClassName) {
        this.linkClassName = linkClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<? extends Baselink> getLinkClass() {
        return linkClass;
    }

    public <T extends BaselinkCreate> T setLinkClass(Class<? extends Baselink> linkClass) {
        this.linkClass = linkClass;
        return (T) this;
    }
}
