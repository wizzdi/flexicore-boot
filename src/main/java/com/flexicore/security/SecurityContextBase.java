/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.security;


import com.flexicore.model.SecurityOperation;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.flexicore.model.security.SecurityPolicy;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;


public class SecurityContextBase<ST extends SecurityTenant,U extends SecurityUser,O extends SecurityOperation,R extends Role> {


	private List<ST> tenants;
	private U user;
	private O operation;
	private Map<String,List<R>> roleMap;
	private boolean impersonated;
	private OffsetDateTime expiresDate;
	private ST tenantToCreateIn;
	private List<SecurityPolicy> securityPolicies;

	public SecurityContextBase() {
	}

	public List<ST> getTenants() {
		return tenants;
	}

	public <T extends SecurityContextBase<ST, U, O, R>> T setTenants(List<ST> tenants) {
		this.tenants = tenants;
		return (T) this;
	}

	public U getUser() {
		return user;
	}

	public <T extends SecurityContextBase<ST, U, O, R>> T setUser(U user) {
		this.user = user;
		return (T) this;
	}

	public O getOperation() {
		return operation;
	}

	public <T extends SecurityContextBase<ST, U, O, R>> T setOperation(O operation) {
		this.operation = operation;
		return (T) this;
	}

	public Map<String, List<R>> getRoleMap() {
		return roleMap;
	}

	public <T extends SecurityContextBase<ST, U, O, R>> T setRoleMap(Map<String, List<R>> roleMap) {
		this.roleMap = roleMap;
		return (T) this;
	}

	public boolean isImpersonated() {
		return impersonated;
	}

	public <T extends SecurityContextBase<ST, U, O, R>> T setImpersonated(boolean impersonated) {
		this.impersonated = impersonated;
		return (T) this;
	}

	public OffsetDateTime getExpiresDate() {
		return expiresDate;
	}

	public <T extends SecurityContextBase<ST, U, O, R>> T setExpiresDate(OffsetDateTime expiresDate) {
		this.expiresDate = expiresDate;
		return (T) this;
	}

	public ST getTenantToCreateIn() {
		return tenantToCreateIn;
	}

	public <T extends SecurityContextBase<ST, U, O, R>> T setTenantToCreateIn(ST tenantToCreateIn) {
		this.tenantToCreateIn = tenantToCreateIn;
		return (T) this;
	}

	public List<SecurityPolicy> getSecurityPolicies() {
		return securityPolicies;
	}

	public <T extends SecurityContextBase<ST, U, O, R>> T setSecurityPolicies(List<SecurityPolicy> securityPolicies) {
		this.securityPolicies = securityPolicies;
		return (T) this;
	}
}
