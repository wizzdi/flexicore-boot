package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;
import com.flexicore.interfaces.dynamic.FieldInfo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//import io.swagger.annotations.ApiModelProperty;


@Entity
public class AccessRef {

    @Id
    private String filterId;

    @FieldInfo(displayName = "access",description = "string filter",mandatory = true)
    private IOperation.Access id;
    @ManyToOne(targetEntity = FilteringInformationHolder.class,cascade = CascadeType.MERGE)
    @JsonIgnore
    private FilteringInformationHolder filteringInformationHolder;

    public AccessRef() {
    }

    ////@ApiModelProperty("Id of the Baseclass")
    public IOperation.Access getId() {
        return id;
    }


    public AccessRef setId(IOperation.Access id) {
        this.id = id;
        return this;
    }

    public AccessRef(IOperation.Access id) {
        this.id = id;
    }

    @Id
    public String getFilterId() {
        return filterId;
    }

    public AccessRef setFilterId(String filterId) {
        this.filterId = filterId;
        return this;
    }

    @ManyToOne(targetEntity = FilteringInformationHolder.class,cascade = CascadeType.MERGE)
    @JsonIgnore
    public FilteringInformationHolder getFilteringInformationHolder() {
        return filteringInformationHolder;
    }

    public AccessRef setFilteringInformationHolder(FilteringInformationHolder filteringInformationHolder) {
        this.filteringInformationHolder = filteringInformationHolder;
        return this;
    }

    public void prepareForSave(FilteringInformationHolder filteringInformationHolder) {
        filterId = Baseclass.getBase64ID();
        this.filteringInformationHolder = filteringInformationHolder;

    }
}
