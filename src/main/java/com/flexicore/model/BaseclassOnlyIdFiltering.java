package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.FieldInfo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//import io.swagger.annotations.ApiModelProperty;


@Entity
public class BaseclassOnlyIdFiltering {

    @Id
    private String filterId;

    @FieldInfo(displayName = "string",description = "string filter",mandatory = true)
    private String id;
    @ManyToOne(targetEntity = FilteringInformationHolder.class,cascade = CascadeType.MERGE)
    @JsonIgnore
    private FilteringInformationHolder filteringInformationHolder;

    public BaseclassOnlyIdFiltering() {
    }

    ////@ApiModelProperty("Id of the Baseclass")
    public String getId() {
        return id;
    }


    public BaseclassOnlyIdFiltering setId(String id) {
        this.id = id;
        return this;
    }

    public BaseclassOnlyIdFiltering(String id) {
        this.id = id;
    }

    @Id
    public String getFilterId() {
        return filterId;
    }

    public BaseclassOnlyIdFiltering setFilterId(String filterId) {
        this.filterId = filterId;
        return this;
    }

    @ManyToOne(targetEntity = FilteringInformationHolder.class,cascade = CascadeType.MERGE)
    @JsonIgnore
    public FilteringInformationHolder getFilteringInformationHolder() {
        return filteringInformationHolder;
    }

    public BaseclassOnlyIdFiltering setFilteringInformationHolder(FilteringInformationHolder filteringInformationHolder) {
        this.filteringInformationHolder = filteringInformationHolder;
        return this;
    }

    public void prepareForSave(FilteringInformationHolder filteringInformationHolder) {
        filterId = Baseclass.getBase64ID();
        this.filteringInformationHolder = filteringInformationHolder;

    }
}
