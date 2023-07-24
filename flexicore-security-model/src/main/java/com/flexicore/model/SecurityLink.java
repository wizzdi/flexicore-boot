/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;


import com.flexicore.annotations.IOperation;
import jakarta.persistence.*;


@Entity
public class SecurityLink extends SecuredBasic {


	@ManyToOne(targetEntity = Baseclass.class)
	private Baseclass baseclass;

	@ManyToOne(targetEntity = PermissionGroup.class)
	private PermissionGroup permissionGroup;
	@ManyToOne(targetEntity = Clazz.class)
	private Clazz clazz;

	@ManyToOne(targetEntity = SecurityOperation.class)
	private SecurityOperation operation;

	@Enumerated(EnumType.STRING)
	private IOperation.Access access;


	@ManyToOne(targetEntity = Baseclass.class)
	public Baseclass getBaseclass() {
		return baseclass;
	}

	public <T extends SecurityLink> T setBaseclass(Baseclass baseclass) {
		this.baseclass = baseclass;
		return (T) this;
	}

	@Transient
	public SecurityEntity getSecurityEntity(){
		return null;
	}

	@ManyToOne(targetEntity = SecurityOperation.class)
	public SecurityOperation getOperation() {
		return operation;
	}

	public <T extends SecurityLink> T setOperation(SecurityOperation operation) {
		this.operation = operation;
		return (T) this;
	}

	@Enumerated(EnumType.STRING)
	public IOperation.Access getAccess() {
		return access;
	}

	public <T extends SecurityLink> T setAccess(IOperation.Access access) {
		this.access = access;
		return (T) this;
	}

	@ManyToOne(targetEntity = PermissionGroup.class)
	public PermissionGroup getPermissionGroup() {
		return permissionGroup;
	}

	public <T extends SecurityLink> T setPermissionGroup(PermissionGroup permissionGroup) {
		this.permissionGroup = permissionGroup;
		return (T) this;
	}

	@ManyToOne(targetEntity = Clazz.class)
	public Clazz getClazz() {
		return clazz;
	}

	public <T extends SecurityLink> T setClazz(Clazz clazz) {
		this.clazz = clazz;
		return (T) this;
	}
}
