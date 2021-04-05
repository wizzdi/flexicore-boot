package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.PaginationFilter;

import java.util.Set;

public class DynamicExecutionFilter extends PaginationFilter {


	private BasicPropertiesFilter basicPropertiesFilter;
	private Set<String> canonicalNames;
	private Set<String> methodNames;
	private Set<String> onlyIds;


	public Set<String> getCanonicalNames() {
		return canonicalNames;
	}

	public <T extends DynamicExecutionFilter> T setCanonicalNames(Set<String> canonicalNames) {
		this.canonicalNames = canonicalNames;
		return (T) this;
	}

	public Set<String> getMethodNames() {
		return methodNames;
	}

	public <T extends DynamicExecutionFilter> T setMethodNames(Set<String> methodNames) {
		this.methodNames = methodNames;
		return (T) this;
	}

	public Set<String> getOnlyIds() {
		return onlyIds;
	}

	public <T extends DynamicExecutionFilter> T setOnlyIds(Set<String> onlyIds) {
		this.onlyIds = onlyIds;
		return (T) this;
	}

	public BasicPropertiesFilter getBasicPropertiesFilter() {
		return basicPropertiesFilter;
	}

	public <T extends DynamicExecutionFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
		this.basicPropertiesFilter = basicPropertiesFilter;
		return (T) this;
	}
}
