package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.TypeRetention;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TenantToUserFilter extends BaselinkFilter {

	private Set<String> usersIds=new HashSet<>();
	@TypeRetention(SecurityUser.class)
	@JsonIgnore
	private List<SecurityUser> securityUsers;
	private Set<String> tenantsIds=new HashSet<>();
	@JsonIgnore
	@TypeRetention(SecurityTenant.class)
	private List<SecurityTenant> securityTenants;

	public Set<String> getUsersIds() {
		return usersIds;
	}

	public <T extends TenantToUserFilter> T setUsersIds(Set<String> usersIds) {
		this.usersIds = usersIds;
		return (T) this;
	}

	@JsonIgnore
	public List<SecurityUser> getSecurityUsers() {
		return securityUsers;
	}

	public <T extends TenantToUserFilter> T setSecurityUsers(List<SecurityUser> securityUsers) {
		this.securityUsers = securityUsers;
		return (T) this;
	}

	public Set<String> getTenantsIds() {
		return tenantsIds;
	}

	public <T extends TenantToUserFilter> T setTenantsIds(Set<String> tenantsIds) {
		this.tenantsIds = tenantsIds;
		return (T) this;
	}

	@JsonIgnore
	public List<SecurityTenant> getSecurityTenants() {
		return securityTenants;
	}

	public <T extends TenantToUserFilter> T setSecurityTenants(List<SecurityTenant> securityTenants) {
		this.securityTenants = securityTenants;
		return (T) this;
	}
}
