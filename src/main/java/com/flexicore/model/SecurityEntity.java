/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.security.SecurityContext;

import javax.persistence.Entity;


@SuppressWarnings("serial")
@Entity

public class SecurityEntity extends Baseclass{


	public SecurityEntity() {
	}

	public SecurityEntity(String name, SecurityContext securityContext) {
		super(name, securityContext);
	}





}
