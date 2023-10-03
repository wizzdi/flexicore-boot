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

	@ManyToOne(targetEntity = OperationGroup.class)
	private OperationGroup operationGroup;
	@ManyToOne(targetEntity = SecurityLinkGroup.class)
	private SecurityLinkGroup securityLinkGroup;

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

	@ManyToOne(targetEntity = OperationGroup.class)
	public OperationGroup getOperationGroup() {
		return operationGroup;
	}

	public <T extends SecurityLink> T setOperationGroup(OperationGroup operationGroup) {
		this.operationGroup = operationGroup;
		return (T) this;
	}

	@ManyToOne(targetEntity = SecurityLinkGroup.class)
	public SecurityLinkGroup getSecurityLinkGroup() {
		return securityLinkGroup;
	}

	public <T extends SecurityLink> T setSecurityLinkGroup(SecurityLinkGroup securityLinkGroup) {
		this.securityLinkGroup = securityLinkGroup;
		return (T) this;
	}
}
