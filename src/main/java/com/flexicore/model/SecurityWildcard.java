/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;



import com.flexicore.security.SecurityContextBase;

import javax.persistence.Entity;

@Entity
public class SecurityWildcard extends Baseclass {



	public SecurityWildcard() {
	}

	public SecurityWildcard(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}

}
