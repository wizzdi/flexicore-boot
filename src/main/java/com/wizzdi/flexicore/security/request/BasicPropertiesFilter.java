package com.wizzdi.flexicore.security.request;

import java.util.Set;

public class BasicPropertiesFilter {

	private String nameLike;
	private Set<String> names;

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
}
