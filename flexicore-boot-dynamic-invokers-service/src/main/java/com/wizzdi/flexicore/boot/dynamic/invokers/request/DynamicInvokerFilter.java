package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;

import java.util.Set;

public class DynamicInvokerFilter extends BaseclassFilter {

	private BasicPropertiesFilter basicPropertiesFilter;
	private Set<String> invokerTypes;
	private String methodNameLike;
	private Set<String> pluginIds;
	private Boolean includeCoreInvokers;
	private Set<String> handlingTypes;
	private String handlingTypeLike;
	private boolean handlingTypeLikeCaseSensitive;



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

	public BasicPropertiesFilter getBasicPropertiesFilter() {
		return basicPropertiesFilter;
	}

	public <T extends DynamicInvokerFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
		this.basicPropertiesFilter = basicPropertiesFilter;
		return (T) this;
	}

	public Set<String> getPluginIds() {
		return pluginIds;
	}

	public <T extends DynamicInvokerFilter> T setPluginIds(Set<String> pluginIds) {
		this.pluginIds = pluginIds;
		return (T) this;
	}

	public Set<String> getHandlingTypes() {
		return handlingTypes;
	}

	public <T extends DynamicInvokerFilter> T setHandlingTypes(Set<String> handlingTypes) {
		this.handlingTypes = handlingTypes;
		return (T) this;
	}

	public String getHandlingTypeLike() {
		return handlingTypeLike;
	}

	public <T extends DynamicInvokerFilter> T setHandlingTypeLike(String handlingTypeLike) {
		this.handlingTypeLike = handlingTypeLike;
		return (T) this;
	}

	public boolean isHandlingTypeLikeCaseSensitive() {
		return handlingTypeLikeCaseSensitive;
	}

	public <T extends DynamicInvokerFilter> T setHandlingTypeLikeCaseSensitive(boolean handlingTypeLikeCaseSensitive) {
		this.handlingTypeLikeCaseSensitive = handlingTypeLikeCaseSensitive;
		return (T) this;
	}

	public Boolean getIncludeCoreInvokers() {
		return includeCoreInvokers;
	}

	public <T extends DynamicInvokerFilter> T setIncludeCoreInvokers(Boolean includeCoreInvokers) {
		this.includeCoreInvokers = includeCoreInvokers;
		return (T) this;
	}
}
