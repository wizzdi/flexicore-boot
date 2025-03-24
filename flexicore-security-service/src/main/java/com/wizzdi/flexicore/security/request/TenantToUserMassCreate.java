package com.wizzdi.flexicore.security.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TenantToUserMassCreate {
	@NotEmpty
	@NotNull
	@Valid
	private List<TenantToUserCreate> tenantToUserCreates;

	public List<TenantToUserCreate> getTenantToUserCreates() {
		return tenantToUserCreates;
	}

	public <T extends TenantToUserMassCreate> T setTenantToUserCreates(List<TenantToUserCreate> roleToUserCreates) {
		this.tenantToUserCreates = roleToUserCreates;
		return (T) this;
	}
}
