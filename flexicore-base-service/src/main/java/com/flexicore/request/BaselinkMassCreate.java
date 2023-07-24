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
    private String leftsideTypeClassName;
    @JsonIgnore
    private Class<?> leftsideType;
    private String rightsideTypeClassName;
    @JsonIgnore
    private Class<?> rightsideType;
    private Set<String> rightsideIds=new HashSet<>();
    @JsonIgnore
    private List<Baseclass> rightside;
    private String valueId;
    @JsonIgnore
    private Baseclass value;
    private String simpleValue;
    private String linkClassName;
    @JsonIgnore
    private Class<? extends SecuredBasic> linkClass;


    public Set<String> getLeftsideIds() {
        return leftsideIds;
    }

    public <T extends SecuredBasicMassCreate> T setLeftsideIds(Set<String> leftsideIds) {
        this.leftsideIds = leftsideIds;
        return (T) this;
    }

    public List<Baseclass> getLeftside() {
        return leftside;
    }

    public <T extends SecuredBasicMassCreate> T setLeftside(List<Baseclass> leftside) {
        this.leftside = leftside;
        return (T) this;
    }

    public Set<String> getRightsideIds() {
        return rightsideIds;
    }

    public <T extends SecuredBasicMassCreate> T setRightsideIds(Set<String> rightsideIds) {
        this.rightsideIds = rightsideIds;
        return (T) this;
    }

    public List<Baseclass> getRightside() {
        return rightside;
    }

    public <T extends SecuredBasicMassCreate> T setRightside(List<Baseclass> rightside) {
        this.rightside = rightside;
        return (T) this;
    }

    public String getValueId() {
        return valueId;
    }

    public <T extends SecuredBasicMassCreate> T setValueId(String valueId) {
        this.valueId = valueId;
        return (T) this;
    }

    public Baseclass getValue() {
        return value;
    }

    public <T extends SecuredBasicMassCreate> T setValue(Baseclass value) {
        this.value = value;
        return (T) this;
    }

    public String getSimpleValue() {
        return simpleValue;
    }

    public <T extends SecuredBasicMassCreate> T setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
        return (T) this;
    }

    public String getLinkClassName() {
        return linkClassName;
    }

    public <T extends SecuredBasicMassCreate> T setLinkClassName(String linkClassName) {
        this.linkClassName = linkClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<? extends SecuredBasic> getLinkClass() {
        return linkClass;
    }

    public <T extends SecuredBasicMassCreate> T setLinkClass(Class<? extends SecuredBasic> linkClass) {
        this.linkClass = linkClass;
        return (T) this;
    }

    public String getLeftsideTypeClassName() {
        return leftsideTypeClassName;
    }

    public <T extends SecuredBasicMassCreate> T setLeftsideTypeClassName(String leftsideTypeClassName) {
        this.leftsideTypeClassName = leftsideTypeClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<?> getLeftsideType() {
        return leftsideType;
    }

    public <T extends SecuredBasicMassCreate> T setLeftsideType(Class<?> leftsideType) {
        this.leftsideType = leftsideType;
        return (T) this;
    }

    public String getRightsideTypeClassName() {
        return rightsideTypeClassName;
    }

    public <T extends SecuredBasicMassCreate> T setRightsideTypeClassName(String rightsideTypeClassName) {
        this.rightsideTypeClassName = rightsideTypeClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<?> getRightsideType() {
        return rightsideType;
    }

    public <T extends SecuredBasicMassCreate> T setRightsideType(Class<?> rightsideType) {
        this.rightsideType = rightsideType;
        return (T) this;
    }
}
