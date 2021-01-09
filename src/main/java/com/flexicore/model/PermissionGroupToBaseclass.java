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

public class PermissionGroupToBaseclass extends Baselink {



	public PermissionGroupToBaseclass() {
	}

	public PermissionGroupToBaseclass(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}

	@ManyToOne(targetEntity = PermissionGroup.class)
	@Override
	public PermissionGroup getLeftside() {
		return (PermissionGroup) super.getLeftside();
	}

	public void setLeftside(PermissionGroup leftside) {
		super.setLeftside(leftside);
	}



	public void setPermissionGroup(PermissionGroup operation) {
		this.leftside=operation;
	}

	@ManyToOne(targetEntity = Baseclass.class)
	@Override
	public Baseclass getRightside() {
		return super.getRightside();
	}

	@Override
	public void setRightside(Baseclass rightside) {
		// TODO Auto-generated method stub
		super.setRightside(rightside);
	}







}
