package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;

public class ExecuteDynamicExecution {
	private String dynamicExecutionId;
	@JsonIgnore
	private DynamicExecution dynamicExecution;

	public String getDynamicExecutionId() {
		return dynamicExecutionId;
	}

	public <T extends ExecuteDynamicExecution> T setDynamicExecutionId(String dynamicExecutionId) {
		this.dynamicExecutionId = dynamicExecutionId;
		return (T) this;
	}

	@JsonIgnore
	public DynamicExecution getDynamicExecution() {
		return dynamicExecution;
	}

	public <T extends ExecuteDynamicExecution> T setDynamicExecution(DynamicExecution dynamicExecution) {
		this.dynamicExecution = dynamicExecution;
		return (T) this;
	}
}
