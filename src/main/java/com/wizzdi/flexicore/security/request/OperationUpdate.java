package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Operation;

public class OperationUpdate extends OperationCreate{

	private String id;
	@JsonIgnore
	private Operation operation;

	public String getId() {
		return id;
	}

	public <T extends OperationUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public Operation getOperation() {
		return operation;
	}

	public <T extends OperationUpdate> T setOperation(Operation operation) {
		this.operation = operation;
		return (T) this;
	}
}
