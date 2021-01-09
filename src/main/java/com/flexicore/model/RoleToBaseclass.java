/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.security.SecurityContextBase;


import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@AnnotatedClazz(Category="Premissions", Name="UserToBaseClass", Description="User Premission on Baseclass")
@Entity
public class RoleToBaseclass extends SecurityLink {


	public RoleToBaseclass() {
	}

	public RoleToBaseclass(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}

	@ManyToOne(targetEntity = Role.class)
	@Override
	public Role getLeftside() {
		// TODO Auto-generated method stub
		return (Role) super.getLeftside();
	}
	public void setRole(Role role) {
		this.leftside=role;
	}

	@ManyToOne(targetEntity = Baseclass.class)
	@Override
	public Baseclass getRightside() {
		// TODO Auto-generated method stub
		return super.getRightside();
	}

	public void setBaseclass(Baseclass baseclass) {
		this.rightside=baseclass;
	}




}
