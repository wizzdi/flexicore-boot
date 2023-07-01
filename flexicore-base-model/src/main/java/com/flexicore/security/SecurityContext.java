package com.flexicore.security;

import com.flexicore.model.Operation;
import com.flexicore.model.Role;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;

import java.util.List;

public class SecurityContext extends SecurityContextBase<Tenant,User, Operation, Role> {
	private boolean totpVerified;

	public SecurityContext() {
	}

	public SecurityContext(List<Tenant>tenants,User user,Operation operation,Tenant tenantToCreateIn) {
		this.setTenants(tenants);
		this.setUser(user);
		this.setOperation(operation);
		this.setTenantToCreateIn(tenantToCreateIn);
	}

	public boolean isTotpVerified() {
		return totpVerified;
	}

	public <T extends SecurityContext> T setTotpVerified(boolean totpVerified) {
		this.totpVerified = totpVerified;
		return (T) this;
	}

}
