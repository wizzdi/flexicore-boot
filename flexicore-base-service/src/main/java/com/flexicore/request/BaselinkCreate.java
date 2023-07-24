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
    private Class<? extends SecuredBasic> linkClass;

    public String getName() {
        return name;
    }

    public <T extends SecuredBasicCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends SecuredBasicCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getLeftsideId() {
        return leftsideId;
    }

    public <T extends SecuredBasicCreate> T setLeftsideId(String leftsideId) {
        this.leftsideId = leftsideId;
        return (T) this;
    }

    @JsonIgnore
    public Baseclass getLeftside() {
        return leftside;
    }

    public <T extends SecuredBasicCreate> T setLeftside(Baseclass leftside) {
        this.leftside = leftside;
        return (T) this;
    }

    public String getRightsideId() {
        return rightsideId;
    }

    public <T extends SecuredBasicCreate> T setRightsideId(String rightsideId) {
        this.rightsideId = rightsideId;
        return (T) this;
    }

    @JsonIgnore
    public Baseclass getRightside() {
        return rightside;
    }

    public <T extends SecuredBasicCreate> T setRightside(Baseclass rightside) {
        this.rightside = rightside;
        return (T) this;
    }

    public String getValueId() {
        return valueId;
    }

    public <T extends SecuredBasicCreate> T setValueId(String valueId) {
        this.valueId = valueId;
        return (T) this;
    }

    @JsonIgnore
    public Baseclass getValue() {
        return value;
    }

    public <T extends SecuredBasicCreate> T setValue(Baseclass value) {
        this.value = value;
        return (T) this;
    }

    public String getSimpleValue() {
        return simpleValue;
    }

    public <T extends SecuredBasicCreate> T setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
        return (T) this;
    }

    public String getLinkClassName() {
        return linkClassName;
    }

    public <T extends SecuredBasicCreate> T setLinkClassName(String linkClassName) {
        this.linkClassName = linkClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<? extends SecuredBasic> getLinkClass() {
        return linkClass;
    }

    public <T extends SecuredBasicCreate> T setLinkClass(Class<? extends SecuredBasic> linkClass) {
        this.linkClass = linkClass;
        return (T) this;
    }
}
