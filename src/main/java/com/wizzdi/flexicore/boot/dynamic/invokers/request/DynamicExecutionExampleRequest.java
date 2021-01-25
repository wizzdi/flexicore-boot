package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DynamicExecutionExampleRequest {
	private String id;
	@JsonIgnore
	private Class<?> clazz;

	public String getId() {
		return id;
	}

	public <T extends DynamicExecutionExampleRequest> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public Class<?> getClazz() {
		return clazz;
	}

	public <T extends DynamicExecutionExampleRequest> T setClazz(Class<?> clazz) {
		this.clazz = clazz;
		return (T) this;
	}
}
