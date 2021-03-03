package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.data.jsoncontainers.SortingOrder;
import com.flexicore.interfaces.dynamic.FieldInfo;
import io.swagger.v3.oas.annotations.media.Schema;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Schema(description = "Use for sorting, includes field name and the direction of sorting")
public class SortParameter {

	@Id
	private String filteringId;

	@ManyToOne(targetEntity = FilteringInformationHolder.class)
	@JsonIgnore
	private FilteringInformationHolder filteringInformationHolder;

	@FieldInfo(displayName = "columnName",description = "columnName to sort by")
	private String name;
	@FieldInfo(displayName = "sortingOrder",description = "sorting order")
	private SortingOrder sortingOrder;
	
	
	
	
	public SortParameter() {
		super();
		filteringId=Baseclass.getBase64ID();

	}
	public SortParameter(String name, SortingOrder sortingOrder) {
		this();
		this.name = name;
		this.sortingOrder = sortingOrder;
	}
	@Schema(description = "Name of the column to sort by")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Schema(description = "Ascending or descending order, alphabetically")
	public SortingOrder getSortingOrder() {
		return sortingOrder;
	}
	public void setSortingOrder(SortingOrder order) {
		this.sortingOrder = order;
	}

	@Id
	public String getFilteringId() {
		return filteringId;
	}

	public SortParameter setFilteringId(String filteringId) {
		this.filteringId = filteringId;
		return this;
	}

	@ManyToOne(targetEntity = FilteringInformationHolder.class)
	@JsonIgnore

	public FilteringInformationHolder getFilteringInformationHolder() {
		return filteringInformationHolder;
	}

	public SortParameter setFilteringInformationHolder(FilteringInformationHolder filteringInformationHolder) {
		this.filteringInformationHolder = filteringInformationHolder;
		return this;
	}

	public void prepareForSave(FilteringInformationHolder filteringInformationHolder) {
		this.filteringInformationHolder=filteringInformationHolder;

	}
}
