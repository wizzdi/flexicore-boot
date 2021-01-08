package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.TenantToBaseClassPremission;

public class TenantToBaseclassPermissionUpdate extends TenantToBaseclassPermissionCreate{

	private String id;
	@JsonIgnore
	private TenantToBaseClassPremission tenantToBaseclassPermission;

	public String getId() {
		return id;
	}

	public <T extends TenantToBaseclassPermissionUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public TenantToBaseClassPremission getTenantToBaseclassPermission() {
		return tenantToBaseclassPermission;
	}

	public <T extends TenantToBaseclassPermissionUpdate> T setTenantToBaseclassPermission(TenantToBaseClassPremission tenantToBaseclassPermission) {
		this.tenantToBaseclassPermission = tenantToBaseclassPermission;
		return (T) this;
	}
}
