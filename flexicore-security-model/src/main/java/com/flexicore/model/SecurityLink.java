/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.model.Access;
import com.wizzdi.segmantix.api.model.ISecurity;
import jakarta.persistence.*;


@Entity
public class SecurityLink extends Baseclass implements ISecurity {

	private String securedId;
	private String securedType;


	@ManyToOne(targetEntity = PermissionGroup.class)
	private PermissionGroup permissionGroup;


	@ManyToOne(targetEntity = SecurityOperation.class)
	private SecurityOperation operation;

	@ManyToOne(targetEntity = OperationGroup.class)
	private OperationGroup operationGroup;
	@ManyToOne(targetEntity = SecurityLinkGroup.class)
	private SecurityLinkGroup securityLinkGroup;

	@Enumerated(EnumType.STRING)
	private Access access;





	@JsonIgnore
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
	public Access getAccess() {
		return access;
	}

	public <T extends SecurityLink> T setAccess(Access access) {
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

	@Override
	public String getSecuredId() {
		return securedId;
	}

	public <T extends SecurityLink> T setSecuredId(String securedId) {
		this.securedId = securedId;
		return (T) this;
	}

	@Override
	public String getSecuredType() {
		return securedType;
	}

	@Transient
	@JsonIgnore
	@Override
	public IInstanceGroup getInstanceGroup() {
		return permissionGroup;
	}

	public <T extends SecurityLink> T setSecuredType(String securedType) {
		this.securedType = securedType;
		return (T) this;
	}
}
