/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;



import com.flexicore.security.SecurityContextBase;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@SuppressWarnings("serial")
@Entity

public class SecurityLink extends Baselink {


	public SecurityLink() {
	}

	public SecurityLink(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}

	@ManyToOne(targetEntity = SecurityEntity.class)
	@Override
	public SecurityEntity getLeftside() {
		// TODO Auto-generated method stub
		return (SecurityEntity) super.getLeftside();
	}
	public void setLeftside(SecurityEntity leftside) {
		this.leftside = leftside;
	}


	@ManyToOne(targetEntity = Baseclass.class)
	@Override
	public Baseclass getRightside() {
		// TODO Auto-generated method stub
		return super.getRightside();
	}

	@Override
	public void setRightside(Baseclass rightside) {
		super.setRightside(rightside);
	}

	@ManyToOne(targetEntity = Baseclass.class)
	//@JoinColumn(name = "value", referencedColumnName = "id")

	@Override
	public Baseclass getValue() {
		// TODO Auto-generated method stub
		return super.getValue();
	}


	@Override
	public void setValue(Baseclass operation) {
		super.setValue(operation);
	}



}
