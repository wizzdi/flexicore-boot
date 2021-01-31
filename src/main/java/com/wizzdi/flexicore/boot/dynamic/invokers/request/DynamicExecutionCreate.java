package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.PluginLoaderIdResolver;

import java.util.Set;

public class DynamicExecutionCreate  {

	private String name;
	private String description;
	private Set<String> serviceCanonicalNames;
	private String methodName;
	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
	@JsonTypeIdResolver(PluginLoaderIdResolver.class)
	private Object executionParametersHolder;

	public String getName() {
		return name;
	}

	public <T extends DynamicExecutionCreate> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public String getDescription() {
		return description;
	}

	public <T extends DynamicExecutionCreate> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}

	public Set<String> getServiceCanonicalNames() {
		return serviceCanonicalNames;
	}

	public <T extends DynamicExecutionCreate> T setServiceCanonicalNames(Set<String> serviceCanonicalNames) {
		this.serviceCanonicalNames = serviceCanonicalNames;
		return (T) this;
	}

	public String getMethodName() {
		return methodName;
	}

	public <T extends DynamicExecutionCreate> T setMethodName(String methodName) {
		this.methodName = methodName;
		return (T) this;
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
	@JsonTypeIdResolver(PluginLoaderIdResolver.class)
	public Object getExecutionParametersHolder() {
		return executionParametersHolder;
	}

	public <T extends DynamicExecutionCreate> T setExecutionParametersHolder(Object executionParametersHolder) {
		this.executionParametersHolder = executionParametersHolder;
		return (T) this;
	}
}
