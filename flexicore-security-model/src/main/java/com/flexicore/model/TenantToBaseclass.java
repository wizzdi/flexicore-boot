/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;



import com.wizzdi.segmantix.api.model.ITenantSecurity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;


@Entity

public class TenantToBaseclass extends SecurityLink implements ITenantSecurity {

	@ManyToOne(targetEntity = SecurityTenant.class)
	private SecurityTenant targetTenant;

	@ManyToOne(targetEntity = SecurityTenant.class)
	public SecurityTenant getTargetTenant() {
		return targetTenant;
	}

	public <T extends TenantToBaseclass> T setTargetTenant(SecurityTenant securityTenant) {
		this.targetTenant = securityTenant;
		return (T) this;
	}

	@Transient
	@Override
	public SecurityEntity getSecurityEntity() {
		return targetTenant;
	}
}
