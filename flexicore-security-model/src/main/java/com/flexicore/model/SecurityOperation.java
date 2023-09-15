/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.annotations.IOperation;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class for Entity: Operation
 * Default Operations are created differently from other classes, methods are automatically extracted from all classes annotated with OperationsInside and IOperation on the method itself
 */

@AnnotatedClazz(Category = "access control", Name = "Operation", Description = "Defines an operation that can be blocked or allowed")
@Entity

public class SecurityOperation extends SecuredBasic {

	private IOperation.Access defaultAccess;

	private String category;

	@JsonIgnore
	@OneToMany(targetEntity = OperationToGroup.class,mappedBy = "operation")
	private List<OperationToGroup> operationToGroups=new ArrayList<>();

	public IOperation.Access getDefaultAccess() {
		return defaultAccess;
	}

	public <T extends SecurityOperation> T setDefaultAccess(IOperation.Access defaultaccess) {
		this.defaultAccess = defaultaccess;
		return (T) this;
	}

	public String getCategory() {
		return category;
	}

	public <T extends SecurityOperation> T setCategory(String category) {
		this.category = category;
		return (T) this;
	}

	@JsonIgnore
	@OneToMany(targetEntity = OperationToGroup.class,mappedBy = "operation")
	public List<OperationToGroup> getOperationToGroups() {
		return operationToGroups;
	}

	public <T extends SecurityOperation> T setOperationToGroups(List<OperationToGroup> operationToGroups) {
		this.operationToGroups = operationToGroups;
		return (T) this;
	}
}
