/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;



import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;



@Entity
public class OperationToClazz extends Basic {

	@ManyToOne(targetEntity = SecurityOperation.class)
	private SecurityOperation operation;
	private String type;

	

	@ManyToOne(targetEntity = SecurityOperation.class)

	public SecurityOperation getOperation() {
		return operation;
	}

	public <T extends OperationToClazz> T setOperation(SecurityOperation operation) {
		this.operation = operation;
		return (T) this;
	}


	public String getType() {
		return type;
	}

	public <T extends OperationToClazz> T setType(String type) {
		this.type = type;
		return (T) this;
	}
}
