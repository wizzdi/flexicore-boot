package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityTenant;

public class SecurityTenantUpdate extends SecurityTenantCreate {

	private String id;
	@JsonIgnore
	private SecurityTenant tenantToUpdate;

	public String getId() {
		return id;
	}

	public <T extends SecurityTenantUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public SecurityTenant getTenantToUpdate() {
		return tenantToUpdate;
	}

	public <T extends SecurityTenantUpdate> T setTenantToUpdate(SecurityTenant tenantToUpdate) {
		this.tenantToUpdate = tenantToUpdate;
		return (T) this;
	}
}
