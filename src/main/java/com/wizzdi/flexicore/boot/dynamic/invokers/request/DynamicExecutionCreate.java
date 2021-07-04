package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.PluginLoaderIdResolver;
import com.wizzdi.flexicore.security.request.BasicCreate;

import java.util.Set;

public class DynamicExecutionCreate  extends BasicCreate {

	private Set<String> serviceCanonicalNames;
	private String methodName;
	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
	@JsonTypeIdResolver(PluginLoaderIdResolver.class)
	private Object executionParametersHolder;
	@JsonIgnore
	private String category;

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

	@JsonIgnore
	public String getCategory() {
		return category;
	}

	public <T extends DynamicExecutionCreate> T setCategory(String category) {
		this.category = category;
		return (T) this;
	}
}
