/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.annotations.FullTextSearch;
import com.flexicore.annotations.sync.SyncOption;
import com.flexicore.security.SecurityContext;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class for Entity: Operation
 *
 */
@SuppressWarnings("serial")
@AnnotatedClazz(Category="core", Name="Tenant", Description="Defines a way to run different isolated virtual database on a single physical one")
@Entity
@FullTextSearch(supported = true)
public class Tenant extends SecurityTenant  {

	private String externalId;
	@ManyToOne(targetEntity = FileResource.class)
	private FileResource icon;

	@JsonIgnore
	@OneToMany(targetEntity = TenantToBaseClassPremission.class,mappedBy="leftside", fetch=FetchType.LAZY)

	private List<TenantToBaseClassPremission> tenantToBaseClassPremissions =new ArrayList<>();

	@OneToMany(targetEntity = TenantToUser.class,mappedBy="leftside", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<TenantToUser> tenantToUser=new ArrayList<>();

	@JsonIgnore
	@OneToMany(targetEntity = TenantToUser.class,mappedBy="leftside", fetch=FetchType.LAZY)
	@SyncOption(sync = false)
	public List<TenantToUser> getTenantToUser() {
		return tenantToUser;
	}

	public void setTenantToUser(List<TenantToUser> tenantToUser) {
		this.tenantToUser = tenantToUser;
	}

	public Tenant() {
		super();
	}

	public Tenant(String name, SecurityContext securityContext) {
		super(name, securityContext);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	@ManyToOne(targetEntity = FileResource.class)
	public FileResource getIcon() {
		return icon;
	}

	public <T extends Tenant> T setIcon(FileResource iconId) {
		this.icon = iconId;
		return (T) this;
	}

	@Override
	public String toString() {
		return "Tenant{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
