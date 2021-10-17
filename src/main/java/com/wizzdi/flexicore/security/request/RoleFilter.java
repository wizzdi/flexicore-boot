package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.TypeRetention;
import com.flexicore.model.SecurityOperation;
import com.flexicore.model.SecurityTenant;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleFilter extends SecurityEntityFilter {

	private Set<String> securityTenantsIds=new HashSet<>();
	@JsonIgnore
	@TypeRetention(SecurityTenant.class)
	private List<SecurityTenant> securityTenants;

	public Set<String> getSecurityTenantsIds() {
		return securityTenantsIds;
	}

	public <T extends RoleFilter> T setSecurityTenantsIds(Set<String> securityTenantsIds) {
		this.securityTenantsIds = securityTenantsIds;
		return (T) this;
	}

	@JsonIgnore
	public List<SecurityTenant> getSecurityTenants() {
		return securityTenants;
	}

	public <T extends RoleFilter> T setSecurityTenants(List<SecurityTenant> securityTenants) {
		this.securityTenants = securityTenants;
		return (T) this;
	}
}
