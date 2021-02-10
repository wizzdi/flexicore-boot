package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.wizzdi.flexicore.security.request.BaseclassFilter;

import java.util.Set;

public class DynamicExecutionFilter extends BaseclassFilter {


	private String nameLike;
	private Set<String> canonicalNames;
	private Set<String> methodNames;
	private Set<String> onlyIds;


	public String getNameLike() {
		return nameLike;
	}

	public <T extends DynamicExecutionFilter> T setNameLike(String nameLike) {
		this.nameLike = nameLike;
		return (T) this;
	}

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
}
