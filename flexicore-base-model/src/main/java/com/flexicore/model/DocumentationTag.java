/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.security.SecurityContext;

import jakarta.persistence.Entity;



@Entity

public class DocumentationTag extends Baseclass {


	
	

	public DocumentationTag() {
		// TODO Auto-generated constructor stub
	}

	public DocumentationTag(String name, SecurityContext securityContext) {
		super(name, securityContext);
	}

}
