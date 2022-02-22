package com.wizzdi.flexicore.security.request;

import com.flexicore.annotations.TypeRetention;

import java.util.Set;

public class BasicPropertiesFilter {

	private String nameLike;
	private boolean nameLikeCaseSensitive;
	@TypeRetention(String.class)
	private Set<String> names;
	private SoftDeleteOption softDelete;
	private DateFilter creationDateFilter;
	private DateFilter updateDateFilter;
	@TypeRetention(String.class)
	private Set<String> onlyIds;

	public String getNameLike() {
		return nameLike;
	}

	public <T extends BasicPropertiesFilter> T setNameLike(String nameLike) {
		this.nameLike = nameLike;
		return (T) this;
	}

	public Set<String> getNames() {
		return names;
	}

	public <T extends BasicPropertiesFilter> T setNames(Set<String> names) {
		this.names = names;
		return (T) this;
	}

	public SoftDeleteOption getSoftDelete() {
		return softDelete;
	}

	public <T extends BasicPropertiesFilter> T setSoftDelete(SoftDeleteOption softDelete) {
		this.softDelete = softDelete;
		return (T) this;
	}

	public DateFilter getCreationDateFilter() {
		return creationDateFilter;
	}

	public <T extends BasicPropertiesFilter> T setCreationDateFilter(DateFilter creationDateFilter) {
		this.creationDateFilter = creationDateFilter;
		return (T) this;
	}

	public DateFilter getUpdateDateFilter() {
		return updateDateFilter;
	}

	public <T extends BasicPropertiesFilter> T setUpdateDateFilter(DateFilter updateDateFilter) {
		this.updateDateFilter = updateDateFilter;
		return (T) this;
	}

	public Set<String> getOnlyIds() {
		return onlyIds;
	}

	public <T extends BasicPropertiesFilter> T setOnlyIds(Set<String> onlyIds) {
		this.onlyIds = onlyIds;
		return (T) this;
	}

	public boolean isNameLikeCaseSensitive() {
		return nameLikeCaseSensitive;
	}

	public <T extends BasicPropertiesFilter> T setNameLikeCaseSensitive(boolean nameLikeCaseSensitive) {
		this.nameLikeCaseSensitive = nameLikeCaseSensitive;
		return (T) this;
	}
}
