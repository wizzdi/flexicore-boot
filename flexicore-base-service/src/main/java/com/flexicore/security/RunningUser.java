/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.security;

import com.flexicore.model.Role;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;
import com.flexicore.model.security.SecurityPolicy;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "used to hold logged-in user data")
public class RunningUser {
	private User user;
	private OffsetDateTime sessionstarted;
	private OffsetDateTime lastaccessed;
	private AuthenticationKey authenticationkey;
	private Boolean loggedin = false;
	private List<Tenant> tenants;
	private Map<String,List<Role>> roles;
	private OffsetDateTime expiresDate;
	private Tenant defaultTenant;
	private List<SecurityPolicy> securityPolicies;
	private boolean impersonated;
	private boolean totpVerified;


	public RunningUser(User user, String jwtToken) {
		this.user = user;
		authenticationkey = new AuthenticationKey(jwtToken);
		sessionstarted = OffsetDateTime.now();

	}

	public RunningUser() {
	}

	@Schema(description =  "UTC time session started")
	public OffsetDateTime getSessionstarted() {
		return sessionstarted;
	}

	public void setSessionstarted(OffsetDateTime sessionstarted) {
		this.sessionstarted = sessionstarted;
	}

	public OffsetDateTime getLastaccessed() {
		return lastaccessed;
	}

	public void setLastaccessed(OffsetDateTime lastaccessed) {
		this.lastaccessed = lastaccessed;
	}

	@Schema(description =  "true if user is logged in")
	public Boolean getLoggedin() {
		return loggedin;
	}

	public void setLoggedin(Boolean loggedin) {
		this.loggedin = loggedin;
	}

	@Schema(description =  "available tenants for this user")

	public List<Tenant> getTenants() {
		return tenants;
	}

	public void setTenants(List<Tenant> tenants) {
		this.tenants = tenants;
	}



	public void setExpiresDate(OffsetDateTime expiresDate) {
		this.expiresDate = expiresDate;
	}

	@Schema(description =  "UTC time the authentication token expires")
	public OffsetDateTime getExpiresDate() {
		return expiresDate;
	}

	@Schema(description =  "authentication key if the user is logged in, to be used in subsequent calls")

	public AuthenticationKey getAuthenticationkey() {
		return authenticationkey;
	}

	@Schema(description =  "Object containin the user information")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setDefaultTenant(Tenant defaultTenant) {

		this.defaultTenant = defaultTenant;
	}

	public Tenant getDefaultTenant() {
		return defaultTenant;
	}

	public boolean isImpersonated() {
		return impersonated;
	}

	public <T extends RunningUser> T setImpersonated(boolean impersonated) {
		this.impersonated = impersonated;
		return (T) this;
	}

	public boolean isTotpVerified() {
		return totpVerified;
	}

	public <T extends RunningUser> T setTotpVerified(boolean totpVerified) {
		this.totpVerified = totpVerified;
		return (T) this;
	}

	public Map<String, List<Role>> getRoles() {
		return roles;
	}

	public <T extends RunningUser> T setRoles(Map<String, List<Role>> roles) {
		this.roles = roles;
		return (T) this;
	}

	public List<SecurityPolicy> getSecurityPolicies() {
		return securityPolicies;
	}

	public <T extends RunningUser> T setSecurityPolicies(List<SecurityPolicy> securityPolicies) {
		this.securityPolicies = securityPolicies;
		return (T) this;
	}
}
