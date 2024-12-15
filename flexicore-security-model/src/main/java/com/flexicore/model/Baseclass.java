/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.wizzdi.flexicore.boot.rest.views.Views;
import com.wizzdi.flexicore.boot.rest.views.Views.Full;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;


@MappedSuperclass
public class Baseclass extends Basic  {


	@ManyToOne(targetEntity = SecurityUser.class)
	@JsonView(Views.ForSwaggerOnly.class)
	protected SecurityUser creator;

	@ManyToOne(targetEntity = SecurityTenant.class)
	private SecurityTenant tenant;

	@ManyToOne(targetEntity = SecurityTenant.class)
	public SecurityTenant getTenant() {
		return tenant;
	}

	public void setTenant(SecurityTenant tenant) {
		this.tenant = tenant;
	}

	@ManyToOne(targetEntity = Clazz.class)
	@JsonView(Full.class)
	protected Clazz clazz;
	// this field is added to to trigger metadatamodel generation , sometimes _.clazz field isn't created.



	@JsonView(Full.class)
	@ManyToOne(targetEntity = Clazz.class)
	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}


	@JsonView(Views.ForSwaggerOnly.class)
	@ManyToOne(targetEntity = SecurityUser.class)
	public SecurityUser getCreator() {
		return creator;
	}

	public void setCreator(SecurityUser creator) {
		this.creator = creator;
	}







}
