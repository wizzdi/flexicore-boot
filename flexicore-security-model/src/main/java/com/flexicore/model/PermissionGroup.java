/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
public class PermissionGroup extends SecuredBasic {



	private String externalId;



	@OneToMany(targetEntity = PermissionGroupToBaseclass.class,mappedBy="permissionGroup")
	@JsonIgnore
	private List<PermissionGroupToBaseclass> links =new ArrayList<>();

	@OneToMany(targetEntity = PermissionGroupToBaseclass.class,mappedBy="permissionGroup")
	@JsonIgnore
	public List<PermissionGroupToBaseclass> getLinks() {
		return links;
	}

	public PermissionGroup setLinks(List<PermissionGroupToBaseclass> permissionGroupToBaseclasses) {
		this.links = permissionGroupToBaseclasses;
		return this;
	}

	public String getExternalId() {
		return externalId;
	}

	public <T extends PermissionGroup> T setExternalId(String externalId) {
		this.externalId = externalId;
		return (T) this;
	}
}
