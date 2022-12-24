package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.FieldInfo;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
public class TenantIdFiltering {

    @Id
    private String filterId;

    @FieldInfo(displayName = "string",description = "string filter",mandatory = true)
    private String id;
    @ManyToOne(targetEntity = FilteringInformationHolder.class)
    @JsonIgnore
    private FilteringInformationHolder filteringInformationHolder;

    public TenantIdFiltering() {
    }

    ////@ApiModelProperty("Id of the Baseclass")
    public String getId() {
        return id;
    }


    public TenantIdFiltering setId(String id) {
        this.id = id;
        return this;
    }

    public TenantIdFiltering(String id) {
        this.id = id;
    }

    @Id
    public String getFilterId() {
        return filterId;
    }

    public TenantIdFiltering setFilterId(String filterId) {
        this.filterId = filterId;
        return this;
    }

    @ManyToOne(targetEntity = FilteringInformationHolder.class)
    @JsonIgnore
    public FilteringInformationHolder getFilteringInformationHolder() {
        return filteringInformationHolder;
    }

    public TenantIdFiltering setFilteringInformationHolder(FilteringInformationHolder filteringInformationHolder) {
        this.filteringInformationHolder = filteringInformationHolder;
        return this;
    }

    public void prepareForSave(FilteringInformationHolder filteringInformationHolder) {
        filterId = Baseclass.getBase64ID();
        this.filteringInformationHolder = filteringInformationHolder;

    }
}
