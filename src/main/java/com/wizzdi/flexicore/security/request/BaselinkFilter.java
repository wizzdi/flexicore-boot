package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.TypeRetention;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.security.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "leftside", fieldType = Baseclass.class, field = "leftsideIds"),
        @IdValid(targetField = "rightside", fieldType = Baseclass.class, field = "rightsideIds"),
        @IdValid(targetField = "values", fieldType = Baseclass.class, field = "valuesIds")


})
public class BaselinkFilter extends BaseclassFilter {

    @JsonIgnore
    @TypeRetention(Baseclass.class)
    private List<Baseclass> leftside;
    private Set<String> leftsideIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(Baseclass.class)
    private List<Baseclass> rightside;
    private Set<String> rightsideIds = new HashSet<>();
    @JsonIgnore
    @TypeRetention(Baseclass.class)
    private List<Baseclass> values;
    private Set<String> valuesIds = new HashSet<>();

    private Set<String> simpleValues;

    @JsonIgnore
    public List<Baseclass> getLeftside() {
        return leftside;
    }

    public <T extends BaselinkFilter> T setLeftside(List<Baseclass> leftside) {
        this.leftside = leftside;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getRightside() {
        return rightside;
    }

    public <T extends BaselinkFilter> T setRightside(List<Baseclass> rightside) {
        this.rightside = rightside;
        return (T) this;
    }

    @JsonIgnore
    public List<Baseclass> getValues() {
        return values;
    }

    public <T extends BaselinkFilter> T setValues(List<Baseclass> values) {
        this.values = values;
        return (T) this;
    }

    public Set<String> getSimpleValues() {
        return simpleValues;
    }

    public <T extends BaselinkFilter> T setSimpleValues(Set<String> simpleValues) {
        this.simpleValues = simpleValues;
        return (T) this;
    }

    public Set<String> getLeftsideIds() {
        return leftsideIds;
    }

    public <T extends BaselinkFilter> T setLeftsideIds(Set<String> leftsideIds) {
        this.leftsideIds = leftsideIds;
        return (T) this;
    }

    public Set<String> getRightsideIds() {
        return rightsideIds;
    }

    public <T extends BaselinkFilter> T setRightsideIds(Set<String> rightsideIds) {
        this.rightsideIds = rightsideIds;
        return (T) this;
    }

    public Set<String> getValuesIds() {
        return valuesIds;
    }

    public <T extends BaselinkFilter> T setValuesIds(Set<String> valuesIds) {
        this.valuesIds = valuesIds;
        return (T) this;
    }
}
