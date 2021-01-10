package com.wizzdi.flexicore.security.response;

import com.flexicore.model.SecurityOperation;

import java.util.List;

public class Operations {

	private final List<SecurityOperation> operations;

	public Operations(List<SecurityOperation> operations) {
		this.operations = operations;
	}

	public List<SecurityOperation> getOperations() {
		return operations;
	}
}
