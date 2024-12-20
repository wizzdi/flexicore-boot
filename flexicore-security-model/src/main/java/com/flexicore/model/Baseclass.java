/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.wizzdi.flexicore.boot.rest.views.Views;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;

import java.util.List;


@MappedSuperclass
public class Baseclass extends Basic  {


	@ManyToOne(targetEntity = SecurityUser.class)
	@JsonView(Views.ForSwaggerOnly.class)
	protected SecurityUser creator;

	@ManyToOne(targetEntity = SecurityTenant.class)
	private SecurityTenant tenant;
	private String securityId;
	@JsonIgnore
	@OneToMany(targetEntity = PermissionGroupToBaseclass.class)
	@JoinColumn(name = "securedId")
	private List<PermissionGroupToBaseclass> relatedPermissionGroups;

	@JsonIgnore
	@ManyToOne(targetEntity = SecurityTenant.class)
	public SecurityTenant getTenant() {
		return tenant;
	}

	public void setTenant(SecurityTenant tenant) {
		this.tenant = tenant;
	}




	@JsonIgnore
	@ManyToOne(targetEntity = SecurityUser.class)
	public SecurityUser getCreator() {
		return creator;
	}

	public void setCreator(SecurityUser creator) {
		this.creator = creator;
	}

	public String getSecurityId() {
		return securityId;
	}

	public <T extends Baseclass> T setSecurityId(String securityId) {
		this.securityId = securityId;
		return (T) this;
	}



	@JsonIgnore
	@OneToMany(targetEntity = PermissionGroupToBaseclass.class)
	@JoinColumn(name = "securedId")
	public List<PermissionGroupToBaseclass> getRelatedPermissionGroups() {
		return relatedPermissionGroups;
	}

	public <T extends Baseclass> T setRelatedPermissionGroups(List<PermissionGroupToBaseclass> relatedPermissionGroups) {
		this.relatedPermissionGroups = relatedPermissionGroups;
		return (T) this;
	}
}
