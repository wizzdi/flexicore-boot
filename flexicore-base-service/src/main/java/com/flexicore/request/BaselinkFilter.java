package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.TypeRetention;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baselink;
import com.flexicore.model.FilteringInformationHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaselinkFilter extends FilteringInformationHolder {

    private Set<String> leftsideIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(Baseclass.class)
    private List<Baseclass> leftside;
    private String leftsideTypeClassName;
    @JsonIgnore
    private Class<?> leftsideType;
    private Set<String> rightsideIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(Baseclass.class)
    private List<Baseclass> rightside;
    private String rightsideTypeClassName;
    @JsonIgnore
    private Class<?> rightsideType;
    private String valueId;
    @JsonIgnore
    private Baseclass value;
    private String simpleValue;
    private String linkClassName;
    @JsonIgnore
    private Class<? extends SecuredBasic> linkClass;

    public BaselinkFilter() {
    }

    public BaselinkFilter(FilteringInformationHolder other) {
        super(other);
    }

    public BaselinkFilter(BaselinkFilter other) {
        super(other);
        this.leftsideIds = other.leftsideIds;
        this.leftside = other.leftside;
        this.rightsideIds = other.rightsideIds;
        this.rightside = other.rightside;
        this.valueId = other.valueId;
        this.value = other.value;
        this.simpleValue = other.simpleValue;
        this.linkClassName = other.linkClassName;
        this.linkClass = other.linkClass;
        this.rightsideType = other.rightsideType;
        this.leftsideType = other.leftsideType;
    }

    public Set<String> getLeftsideIds() {
        return leftsideIds;
    }

    public <T extends SecuredBasicFilter> T setLeftsideIds(Set<String> leftsideIds) {
        this.leftsideIds = leftsideIds;
        return (T) this;
    }

    public List<Baseclass> getLeftside() {
        return leftside;
    }

    public <T extends SecuredBasicFilter> T setLeftside(List<Baseclass> leftside) {
        this.leftside = leftside;
        return (T) this;
    }

    public Set<String> getRightsideIds() {
        return rightsideIds;
    }

    public <T extends SecuredBasicFilter> T setRightsideIds(Set<String> rightsideIds) {
        this.rightsideIds = rightsideIds;
        return (T) this;
    }

    public List<Baseclass> getRightside() {
        return rightside;
    }

    public <T extends SecuredBasicFilter> T setRightside(List<Baseclass> rightside) {
        this.rightside = rightside;
        return (T) this;
    }

    public String getValueId() {
        return valueId;
    }

    public <T extends SecuredBasicFilter> T setValueId(String valueId) {
        this.valueId = valueId;
        return (T) this;
    }

    public Baseclass getValue() {
        return value;
    }

    public <T extends SecuredBasicFilter> T setValue(Baseclass value) {
        this.value = value;
        return (T) this;
    }

    public String getSimpleValue() {
        return simpleValue;
    }

    public <T extends SecuredBasicFilter> T setSimpleValue(String simpleValue) {
        this.simpleValue = simpleValue;
        return (T) this;
    }

    public String getLinkClassName() {
        return linkClassName;
    }

    public <T extends SecuredBasicFilter> T setLinkClassName(String linkClassName) {
        this.linkClassName = linkClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<? extends SecuredBasic> getLinkClass() {
        return linkClass;
    }

    public <T extends SecuredBasicFilter> T setLinkClass(Class<? extends SecuredBasic> linkClass) {
        this.linkClass = linkClass;
        return (T) this;
    }

    @JsonIgnore
    public Class<?> getLeftsideType() {
        return leftsideType;
    }

    public <T extends SecuredBasicFilter> T setLeftsideType(Class<?> leftsideType) {
        this.leftsideType = leftsideType;
        return (T) this;
    }

    @JsonIgnore
    public Class<?> getRightsideType() {
        return rightsideType;
    }

    public <T extends SecuredBasicFilter> T setRightsideType(Class<?> rightsideType) {
        this.rightsideType = rightsideType;
        return (T) this;
    }

    public String getLeftsideTypeClassName() {
        return leftsideTypeClassName;
    }

    public <T extends SecuredBasicFilter> T setLeftsideTypeClassName(String leftsideTypeClassName) {
        this.leftsideTypeClassName = leftsideTypeClassName;
        return (T) this;
    }

    public String getRightsideTypeClassName() {
        return rightsideTypeClassName;
    }

    public <T extends SecuredBasicFilter> T setRightsideTypeClassName(String rightsideTypeClassName) {
        this.rightsideTypeClassName = rightsideTypeClassName;
        return (T) this;
    }
}
