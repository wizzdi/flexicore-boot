/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.IOperation.Access;
import com.flexicore.model.dynamic.DynamicInvoker;
import com.flexicore.security.SecurityContext;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: Operation
 * Default Operations are created differently from other classes, methods are automatically extracted from all classes annotated with OperationsInside and IOperation on the method itself
 */

@AnnotatedClazz(Category = "access control", Name = "Operation", Description = "Defines an operation that can be blocked or allowed")
@Entity

public class Operation extends SecurityOperation  {
	private boolean auditable;
	private Access defaultaccess;
	@ManyToOne(targetEntity = DynamicInvoker.class)
	private DynamicInvoker dynamicInvoker;

	public Operation() {
		super();
	}

	public Operation(String name, SecurityContext securityContext) {
		super(name, securityContext);
	}

	public Operation(IOperation operationannotation, String id) {
		super();
		this.setId(id);
		this.setName(operationannotation.Name());
		this.setDescription(operationannotation.Description());
		this.setDefaultAccess(operationannotation.access());
		this.auditable=operationannotation.auditable();
	}


	public boolean isAuditable() {
		return auditable;
	}

	public Operation setAuditable(boolean auditable) {
		this.auditable = auditable;
		return this;
	}

	@ManyToOne(targetEntity = DynamicInvoker.class)
	public DynamicInvoker getDynamicInvoker() {
		return dynamicInvoker;
	}

	public Operation setDynamicInvoker(DynamicInvoker dynamicInvoker) {
		this.dynamicInvoker = dynamicInvoker;
		return this;
	}

	@Override
	public String toString() {
		return "Operation{" +
				"auditable=" + auditable +
				", defaultaccess=" + defaultaccess +
				", id='" + id + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
