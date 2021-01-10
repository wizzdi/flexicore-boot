package com.wizzdi.flexicore.security.response;

import com.wizzdi.flexicore.security.request.SecurityOperationCreate;

public class OperationScanContext {
	private final SecurityOperationCreate securityOperationCreate;
	private final Class<?>[] relatedClasses;

	public OperationScanContext(SecurityOperationCreate securityOperationCreate, Class<?>[] relatedClasses) {
		this.securityOperationCreate = securityOperationCreate;
		this.relatedClasses = relatedClasses;
	}


	public SecurityOperationCreate getSecurityOperationCreate() {
		return securityOperationCreate;
	}

	public Class<?>[] getRelatedClasses() {
		return relatedClasses;
	}
}
