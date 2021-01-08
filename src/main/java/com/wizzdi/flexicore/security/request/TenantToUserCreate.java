package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityUser;
import com.flexicore.model.Tenant;

public class TenantToUserCreate extends BaselinkCreate{

	private Boolean defaultTenant;
	@JsonIgnore
	private SecurityUser securityUser;
	private String userId;



	public Boolean getDefaultTenant() {
		return defaultTenant;
	}

	public <T extends TenantToUserCreate> T setDefaultTenant(Boolean defaultTenant) {
		this.defaultTenant = defaultTenant;
		return (T) this;
	}

	@JsonIgnore
	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	public <T extends TenantToUserCreate> T setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
		return (T) this;
	}

	public String getUserId() {
		return userId;
	}

	public <T extends TenantToUserCreate> T setUserId(String userId) {
		this.userId = userId;
		return (T) this;
	}

}
