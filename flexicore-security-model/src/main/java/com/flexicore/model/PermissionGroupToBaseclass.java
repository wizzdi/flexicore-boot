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

public class PermissionGroupToBaseclass extends SecuredBasic {


	@ManyToOne(targetEntity = PermissionGroup.class)
	private PermissionGroup permissionGroup;
	@ManyToOne(targetEntity = Baseclass.class)
	private Baseclass baseclass;

	@ManyToOne(targetEntity = PermissionGroup.class)

	public PermissionGroup getPermissionGroup() {
		return permissionGroup;
	}

	public <T extends PermissionGroupToBaseclass> T setPermissionGroup(PermissionGroup permissionGroup) {
		this.permissionGroup = permissionGroup;
		return (T) this;
	}

	@ManyToOne(targetEntity = Baseclass.class)

	public Baseclass getBaseclass() {
		return baseclass;
	}

	public <T extends PermissionGroupToBaseclass> T setBaseclass(Baseclass baseclass) {
		this.baseclass = baseclass;
		return (T) this;
	}
}
