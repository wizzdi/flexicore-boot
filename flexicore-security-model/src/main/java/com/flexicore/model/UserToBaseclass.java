/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;


import com.wizzdi.segmantix.api.model.IUserSecurity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;


@AnnotatedClazz(Category = "Permissions", Name = "RoleToBaseClass", Description = "User Permission on Baseclass")
@Entity

public class UserToBaseclass extends SecurityLink implements IUserSecurity {

	@ManyToOne(targetEntity = SecurityUser.class)
	private SecurityUser user;

	@ManyToOne(targetEntity = SecurityUser.class)
	public SecurityUser getUser() {
		return user;
	}

	public <T extends UserToBaseclass> T setUser(SecurityUser user) {
		this.user = user;
		return (T) this;
	}

	@Transient
	@Override
	public SecurityEntity getSecurityEntity() {
		return user;
	}
}
