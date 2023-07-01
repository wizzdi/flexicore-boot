package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.FieldInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
public class BaseclassLongFiltering {

    @Id
    private String filterId;
    @FieldInfo(displayName = "number",description = "number filter",mandatory = true)

    private long someNumber;
    @ManyToOne(targetEntity = FilteringInformationHolder.class)
    @JsonIgnore
    private FilteringInformationHolder filteringInformationHolder;

    ////@ApiModelProperty("long")
    public long getSomeNumber() {
        return someNumber;
    }

    public BaseclassLongFiltering() {
        filterId=Baseclass.getBase64ID();
    }

    public BaseclassLongFiltering setSomeNumber(long someNumber) {
        this.someNumber = someNumber;
        return this;
    }

    public BaseclassLongFiltering(long someNumber) {
        this.someNumber = someNumber;
    }

    @Id
    public String getFilterId() {
        return filterId;
    }

    public BaseclassLongFiltering setFilterId(String filterId) {
        this.filterId = filterId;
        return this;
    }

    @ManyToOne(targetEntity = FilteringInformationHolder.class)
    @JsonIgnore
    public FilteringInformationHolder getFilteringInformationHolder() {
        return filteringInformationHolder;
    }

    public BaseclassLongFiltering setFilteringInformationHolder(FilteringInformationHolder filteringInformationHolder) {
        this.filteringInformationHolder = filteringInformationHolder;
        return this;
    }

    public void prepareForSave(FilteringInformationHolder filteringInformationHolder) {
        this.filteringInformationHolder=filteringInformationHolder;

    }
}
