/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.security.SecurityContextBase;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity implementation class for Entity: Role
 * Note that the default roles are defines through annotation and all operations are linked to these roles while operations are created.
 *
 */
@SuppressWarnings("serial")
@AnnotatedClazz(Category="access control", Name="Role", Description="Groups users by Operations they are allowed to perform")
@Entity
public class Role extends SecurityEntity  {


	@JsonIgnore
	@OneToMany(mappedBy="leftside", fetch=FetchType.LAZY,targetEntity=RoleToUser.class) //users are subscribed to very few roles.
	private List<RoleToUser> roleToUser =new ArrayList<>();


	@OneToMany(targetEntity = RoleToBaseclass.class,mappedBy="leftside", fetch=FetchType.LAZY)
	@JsonIgnore
	private List<RoleToBaseclass> roleToBaseclass =new ArrayList<>();
	
	public Role() {
		super();
	}

	public Role(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}

	@JsonIgnore
	@OneToMany(mappedBy="leftside", fetch=FetchType.LAZY,targetEntity=RoleToUser.class) //users are subscribed to very few roles.
	public List<RoleToUser> getRoleToUser() {
		return roleToUser;
	}

	public void setRoleToUser(List<RoleToUser> users) {
		this.roleToUser = users;
	}



	@OneToMany(targetEntity = RoleToBaseclass.class,mappedBy="leftside", fetch=FetchType.LAZY)
	@JsonIgnore
	public List<RoleToBaseclass> getRoleToBaseclass() {
		return roleToBaseclass;
	}

	public void setRoleToBaseclass(List<RoleToBaseclass> baseclasses) {
		this.roleToBaseclass = baseclasses;
	}

}
