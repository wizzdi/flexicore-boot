/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.api.model.IInstanceGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

import java.time.OffsetDateTime;


@Entity

public class PermissionGroupToBaseclass extends Baseclass  {


	@ManyToOne(targetEntity = PermissionGroup.class)
	private PermissionGroup permissionGroup;
	private String securedId;
	private String securedType;

	@Column(columnDefinition = "timestamp with time zone")
	private OffsetDateTime securedCreationDate;




	@ManyToOne(targetEntity = PermissionGroup.class)
	public PermissionGroup getPermissionGroup() {
		return permissionGroup;
	}

	public <T extends PermissionGroupToBaseclass> T setPermissionGroup(PermissionGroup permissionGroup) {
		this.permissionGroup = permissionGroup;
		return (T) this;
	}

	public String getSecuredId() {
		return securedId;
	}

	public <T extends PermissionGroupToBaseclass> T setSecuredId(String securedId) {
		this.securedId = securedId;
		return (T) this;
	}

	public String getSecuredType() {
		return securedType;
	}



	public <T extends PermissionGroupToBaseclass> T setSecuredType(String securedType) {
		this.securedType = securedType;
		return (T) this;
	}

	@Column(columnDefinition = "timestamp with time zone")
	public OffsetDateTime getSecuredCreationDate() {
		return securedCreationDate;
	}

	public <T extends PermissionGroupToBaseclass> T setSecuredCreationDate(OffsetDateTime securedCreationDate) {
		this.securedCreationDate = securedCreationDate;
		return (T) this;
	}
}
