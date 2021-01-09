package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityOperation;

public class SecurityOperationUpdate extends SecurityOperationCreate {

	private String id;
	@JsonIgnore
	private SecurityOperation operation;

	public String getId() {
		return id;
	}

	public <T extends SecurityOperationUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public SecurityOperation getOperation() {
		return operation;
	}

	public <T extends SecurityOperationUpdate> T setOperation(SecurityOperation operation) {
		this.operation = operation;
		return (T) this;
	}
}
