package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.wizzdi.flexicore.security.request.BaseclassFilter;

import java.util.Set;

public class DynamicInvokerFilter extends BaseclassFilter {

	private Set<String> invokerTypes;
	private String nameLike;
	private String methodNameLike;

	public String getNameLike() {
		return nameLike;
	}

	public <T extends DynamicInvokerFilter> T setNameLike(String nameLike) {
		this.nameLike = nameLike;
		return (T) this;
	}

	public String getMethodNameLike() {
		return methodNameLike;
	}

	public <T extends DynamicInvokerFilter> T setMethodNameLike(String methodNameLike) {
		this.methodNameLike = methodNameLike;
		return (T) this;
	}

	public Set<String> getInvokerTypes() {
		return invokerTypes;
	}

	public <T extends DynamicInvokerFilter> T setInvokerTypes(Set<String> invokerTypes) {
		this.invokerTypes = invokerTypes;
		return (T) this;
	}
}
