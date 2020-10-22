/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.security.SecurityContext;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")

@Entity

public class TenantToBaseClassPremission extends SecurityLink {



	@ManyToOne(targetEntity = Tenant.class)
	@Override
	public Tenant getLeftside() {
		return (Tenant) super.getLeftside();
	}

	public void setLeftside(Tenant leftside) {
		// TODO Auto-generated method stub
		super.setLeftside(leftside);
	}

	public TenantToBaseClassPremission() {
	}

	public TenantToBaseClassPremission(String name, SecurityContext securityContext) {
		super(name, securityContext);
	}

	public void setTenant(Tenant tenant) {
		this.leftside=tenant;
	}


	@ManyToOne(targetEntity = Baseclass.class)
	@Override
	public Baseclass getRightside() {
		return super.getRightside();
	}

	public void setBaseclass(Baseclass baseclass) {
		this.rightside=baseclass;
	}


}
