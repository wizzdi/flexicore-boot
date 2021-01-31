package com.wizzdi.flexicore.boot.dynamic.invokers.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

public class InvokerBody {

	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
	@JsonTypeIdResolver(PluginLoaderIdResolver.class)
	private Object object;

	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
	@JsonTypeIdResolver(PluginLoaderIdResolver.class)
	public Object getObject() {
		return object;
	}

	public <T extends InvokerBody> T setObject(Object object) {
		this.object = object;
		return (T) this;
	}
}
