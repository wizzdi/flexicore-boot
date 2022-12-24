/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.annotations.IOperation;
import com.flexicore.security.SecurityContextBase;

import jakarta.persistence.Entity;

/**
 * Entity implementation class for Entity: Operation
 * Default Operations are created differently from other classes, methods are automatically extracted from all classes annotated with OperationsInside and IOperation on the method itself
 */
@SuppressWarnings("serial")
@AnnotatedClazz(Category = "access control", Name = "Operation", Description = "Defines an operation that can be blocked or allowed")
@Entity

public class SecurityOperation extends Baseclass {

	private IOperation.Access defaultaccess;


	public SecurityOperation() {
	}

	public SecurityOperation(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}

	public IOperation.Access getDefaultaccess() {
		return defaultaccess;
	}

	public <T extends SecurityOperation> T setDefaultaccess(IOperation.Access defaultaccess) {
		this.defaultaccess = defaultaccess;
		return (T) this;
	}
}
