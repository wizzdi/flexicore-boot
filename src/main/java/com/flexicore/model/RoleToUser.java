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

/**
 * Entity implementation class for Entity: RoleToUser
 *
 */
@SuppressWarnings("serial")
@AnnotatedClazz(Category="access control", Name="RoleToUser", Description="Relates Users  to Roles")
@Entity

public class RoleToUser extends Baselink  {




	@ManyToOne(targetEntity = Role.class)
	@Override
	public Role getLeftside() {
		return (Role) super.getLeftside();
	}

	public void setLeftside(Role leftside) {
		super.setLeftside(leftside);
	}

	public void setRole(Role role) {
		this.leftside=role;
	}


	@ManyToOne(targetEntity = User.class)
	@Override
	public User getRightside() {
		return (User) super.getRightside();
	}
	public void setUser(User user) {
		this.rightside=user;
	}

	public void setRightside(User rightside) {
		super.setRightside(rightside);
	}

	public RoleToUser() {
		super();
	}

	public RoleToUser(String name, SecurityContext securityContext) {
		super(name, securityContext);
	}


}
