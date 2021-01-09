/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;



import com.flexicore.security.SecurityContextBase;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")

@Entity

public class OperationToClazz extends Baselink {



	public OperationToClazz() {
	}

	public OperationToClazz(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}

	@ManyToOne(targetEntity = SecurityOperation.class)
	//@JoinColumn(name = "leftside", referencedColumnName = "id")

	@Override
	public SecurityOperation getLeftside() {
		return (SecurityOperation) super.getLeftside();
	}

	@Override
	public void setLeftside(Baseclass leftside) {
		// TODO Auto-generated method stub
		super.setLeftside(leftside);
	}


	public void setOperation(SecurityOperation operation) {
		this.leftside=operation;
	}

	@ManyToOne(targetEntity = Clazz.class)
	//@JoinColumn(name = "rightside", referencedColumnName = "id")

	@Override
	public Clazz getRightside() {
		return (Clazz)super.getRightside();
	}

	@Override
	public void setRightside(Baseclass rightside) {
		// TODO Auto-generated method stub
		super.setRightside(rightside);
	}


	public void setClazz(Clazz clazz) {
		this.rightside=clazz;
	}





}
