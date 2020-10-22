/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.security.SecurityContext;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@AnnotatedClazz(Category="Premissions", Name="RoleToBaseClass", Description="User Premission on Baseclass")
@Entity

public class UserToBaseClass extends SecurityLink {


	public UserToBaseClass() {
	}

	public UserToBaseClass(String name, SecurityContext securityContext) {
		super(name, securityContext);
	}

	@ManyToOne(targetEntity = User.class)
	@Override
	public User getLeftside() {
		// TODO Auto-generated method stub
		return (User) super.getLeftside();
	}
	public void setUser(User user) {
		this.leftside=user;
	}

	@Override
	public void setLeftside(Baseclass user) {
		this.leftside=user;
	}



	@ManyToOne(targetEntity = Baseclass.class)
	@Override
	public Baseclass getRightside() {
		// TODO Auto-generated method stub
		return super.getRightside();
	}

	@Override
	public void setRightside(Baseclass baseclass) {
		this.rightside=baseclass;
	}


	public void setBaseclass(Baseclass baseclass) {
		this.rightside=baseclass;
	}






}
