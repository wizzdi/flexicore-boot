/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.security.SecurityContextBase;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

//the table name 'user' isn't allowed in Postgresql
@SuppressWarnings("serial")
@Table(name = "UserTable", indexes = {
        @Index(name = "user_email_ix", columnList = "email")
})
@Entity

public class SecurityUser extends SecurityEntity {

	@JsonIgnore
	@OneToMany(targetEntity = RoleToUser.class,mappedBy = "rightside")
	private List<RoleToUser> roles=new ArrayList<>();

	@JsonIgnore
	@OneToMany(targetEntity = UserToBaseClass.class,mappedBy = "leftside")
	private List<UserToBaseClass> userToBaseClasses=new ArrayList<>();

	public SecurityUser() {
	}

	public SecurityUser(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}

	@JsonIgnore
	@OneToMany(targetEntity = RoleToUser.class,mappedBy = "rightside")
	public List<RoleToUser> getRoles() {
		return roles;
	}

	public <T extends SecurityUser> T setRoles(List<RoleToUser> roles) {
		this.roles = roles;
		return (T) this;
	}

	@JsonIgnore
	@OneToMany(targetEntity = UserToBaseClass.class,mappedBy = "leftside")
	public List<UserToBaseClass> getUserToBaseClasses() {
		return userToBaseClasses;
	}

	public <T extends SecurityUser> T setUserToBaseClasses(List<UserToBaseClass> userToBaseClasses) {
		this.userToBaseClasses = userToBaseClasses;
		return (T) this;
	}
}
