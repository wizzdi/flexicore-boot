/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.security.SecurityContextBase;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

//the table name 'user' isn't allowed in Postgresql
@SuppressWarnings("serial")
@Table(name = "UserTable", indexes = {
        @Index(name = "user_email_ix", columnList = "email")
})
@Entity

public class SecurityUser extends SecurityEntity {


	public SecurityUser() {
	}

	public SecurityUser(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}
}
