package com.wizzdi.flexicore.security.response;

import com.flexicore.model.*;

public class DefaultSecurityEntities {

	private final SecurityUser securityUser;
	private final SecurityTenant securityTenant;
	private final Role role;
	private final TenantToUser tenantToUser;
	private final RoleToUser roleToUser;


	public DefaultSecurityEntities(SecurityUser securityUser, SecurityTenant securityTenant, Role role, TenantToUser tenantToUser, RoleToUser roleToUser) {
		this.securityUser = securityUser;
		this.securityTenant = securityTenant;
		this.role = role;
		this.tenantToUser = tenantToUser;
		this.roleToUser = roleToUser;
	}

	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	public SecurityTenant getSecurityTenant() {
		return securityTenant;
	}

	public Role getRole() {
		return role;
	}

	public TenantToUser getTenantToUser() {
		return tenantToUser;
	}

	public RoleToUser getRoleToUser() {
		return roleToUser;
	}
}
