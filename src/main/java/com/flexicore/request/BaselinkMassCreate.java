package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baselink;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaselinkMassCreate {

    private Set<String> leftsideIds=new HashSet<>();
    @JsonIgnore
    private List<Baseclass> leftside;
    private Set<String> rightsideIds=new HashSet<>();
    @JsonIgnore
    private List<Baseclass> rightside;
    private String valueId;
    @JsonIgnore
    private Baseclass value;
    private String simpleValue;
    private String linkClassName;
    @JsonIgnore
    private Class<? extends Baselink> linkClass;


    public Set<String> getLeftsideIds() {
        return leftsideIds;
    }

    public <T extends BaselinkMassCreate> T setLeftsideIds(Set<String> leftsideIds) {
        this.leftsideIds = leftsideIds;
        return (T) this;
    }

    public List<Baseclass> getLeftside() {
        return leftside;
    }

    public <T extends BaselinkMassCreate> T setLeftside(List<Baseclass> leftside) {
        this.leftside = leftside;
        return (T) this;
    }

    public Set<String> getRightsideIds() {
        return rightsideIds;
    }

    public <T extends BaselinkMassCreate> T setRightsideIds(Set<String> rightsideIds) {
        this.rightsideIds = rightsideIds;
        return (T) this;
    }

    public List<Baseclass> getRightside() {
        return rightside;
    }

    public <T extends BaselinkMassCreate> T setRightside(List<Baseclass> rightside) {
        this.rightside = rightside;
        return (T) this;
    }

    public String getValueId() {
        return valueId;
    }

    public <T extends BaselinkMassCreate> T setValueId(String valueId) {
        this.valueId = valueId;
        return (T) this;
    }

    public Baseclass getValue() {
        return value;
    }

    public <T extends BaselinkMassCreate> T setValue(Baseclass value) {
        this.value = value;
        return (T) this;
    }

    public String getSimpleValue() {
        return simpleValue;
    }

    public <T extends BaselinkMassCreate> T setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
        return (T) this;
    }

    public String getLinkClassName() {
        return linkClassName;
    }

    public <T extends BaselinkMassCreate> T setLinkClassName(String linkClassName) {
        this.linkClassName = linkClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<? extends Baselink> getLinkClass() {
        return linkClass;
    }

    public <T extends BaselinkMassCreate> T setLinkClass(Class<? extends Baselink> linkClass) {
        this.linkClass = linkClass;
        return (T) this;
    }
}
