package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.OperationToClazz;

public class OperationToClazzUpdate extends OperationToClazzCreate{

	private String id;
	@JsonIgnore
	private OperationToClazz operationToClazz;

	public String getId() {
		return id;
	}

	public <T extends OperationToClazzUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public OperationToClazz getOperationToClazz() {
		return operationToClazz;
	}

	public <T extends OperationToClazzUpdate> T setOperationToClazz(OperationToClazz operationToClazz) {
		this.operationToClazz = operationToClazz;
		return (T) this;
	}
}
