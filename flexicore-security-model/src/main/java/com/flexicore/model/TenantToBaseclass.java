/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;



import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;


@Entity

public class TenantToBaseclass extends SecurityLink {

	@ManyToOne(targetEntity = SecurityTenant.class)
	private SecurityTenant tenant;

	@ManyToOne(targetEntity = SecurityTenant.class)
	public SecurityTenant getTenant() {
		return tenant;
	}

	public <T extends TenantToBaseclass> T setTenant(SecurityTenant securityTenant) {
		this.tenant = securityTenant;
		return (T) this;
	}

	@Transient
	@Override
	public SecurityEntity getSecurityEntity() {
		return tenant;
	}
}
