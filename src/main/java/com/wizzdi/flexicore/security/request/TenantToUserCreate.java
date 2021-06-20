package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityUser;
import com.flexicore.model.SecurityTenant;

public class TenantToUserCreate extends BaselinkCreate{

	private Boolean defaultTenant;
	@JsonIgnore
	private SecurityUser securityUser;
	private String userId;

	public TenantToUserCreate(TenantToUserCreate other) {
		super(other);
		this.defaultTenant = other.defaultTenant;
		this.securityUser = other.securityUser;
		this.userId = other.userId;
	}

	public TenantToUserCreate() {
	}

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
