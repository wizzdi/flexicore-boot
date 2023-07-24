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


public class SecurityContextBase {


	private List<SecurityTenant> tenants;
	private SecurityUser user;
	private SecurityOperation operation;
	private List<Role> allRoles;
	private Map<String,List<Role>> roleMap;
	private boolean impersonated;
	private OffsetDateTime expiresDate;
	private SecurityTenant tenantToCreateIn;
	private List<SecurityPolicy> securityPolicies;

	private SecurityPermissions securityPermissions;

	public SecurityContextBase() {
	}

	public List<SecurityTenant> getTenants() {
		return tenants;
	}

	public <T extends SecurityContextBase> T setTenants(List<SecurityTenant> tenants) {
		this.tenants = tenants;
		return (T) this;
	}

	public SecurityUser getUser() {
		return user;
	}

	public <T extends SecurityContextBase> T setUser(SecurityUser user) {
		this.user = user;
		return (T) this;
	}

	public SecurityOperation getOperation() {
		return operation;
	}

	public <T extends SecurityContextBase> T setOperation(SecurityOperation operation) {
		this.operation = operation;
		return (T) this;
	}

	public Map<String, List<Role>> getRoleMap() {
		return roleMap;
	}

	public <T extends SecurityContextBase> T setRoleMap(Map<String, List<Role>> roleMap) {
		this.roleMap = roleMap;
		return (T) this;
	}

	public boolean isImpersonated() {
		return impersonated;
	}

	public <T extends SecurityContextBase> T setImpersonated(boolean impersonated) {
		this.impersonated = impersonated;
		return (T) this;
	}

	public OffsetDateTime getExpiresDate() {
		return expiresDate;
	}

	public <T extends SecurityContextBase> T setExpiresDate(OffsetDateTime expiresDate) {
		this.expiresDate = expiresDate;
		return (T) this;
	}

	public SecurityTenant getTenantToCreateIn() {
		return tenantToCreateIn;
	}

	public <T extends SecurityContextBase> T setTenantToCreateIn(SecurityTenant tenantToCreateIn) {
		this.tenantToCreateIn = tenantToCreateIn;
		return (T) this;
	}

	public List<SecurityPolicy> getSecurityPolicies() {
		return securityPolicies;
	}

	public <T extends SecurityContextBase> T setSecurityPolicies(List<SecurityPolicy> securityPolicies) {
		this.securityPolicies = securityPolicies;
		return (T) this;
	}

	public List<Role> getAllRoles() {
		return allRoles;
	}

	public <T extends SecurityContextBase> T setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
		return (T) this;
	}

	public SecurityPermissions getSecurityPermissions() {
		return securityPermissions;
	}

	public <T extends SecurityContextBase> T setSecurityPermissions(SecurityPermissions securityPermissions) {
		this.securityPermissions = securityPermissions;
		return (T) this;
	}
}
