/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.security.SecurityContextBase;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class for Entity: Operation
 *
 */
@SuppressWarnings("serial")
@AnnotatedClazz(Category="core", Name="Tenant", Description="Defines a way to run different isolated virtual database on a single physical one")
@Entity
public class SecurityTenant extends SecurityEntity  {

	private String externalId;


	@JsonIgnore
	@OneToMany(targetEntity = TenantToBaseClassPremission.class,mappedBy="leftside", fetch=FetchType.LAZY)
	private List<TenantToBaseClassPremission> tenantToBaseClassPremissions =new ArrayList<>();

	@OneToMany(targetEntity = TenantToUser.class,mappedBy="leftside", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<TenantToUser> tenantToUser=new ArrayList<>();

	@JsonIgnore
	@OneToMany(targetEntity = TenantToUser.class,mappedBy="leftside", fetch=FetchType.LAZY)
	public List<TenantToUser> getTenantToUser() {
		return tenantToUser;
	}

	public void setTenantToUser(List<TenantToUser> tenantToUser) {
		this.tenantToUser = tenantToUser;
	}

	public SecurityTenant() {
		super();
	}

	public SecurityTenant(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}


	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

}
