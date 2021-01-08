package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityUser;

public class SecurityUserUpdate extends SecurityUserCreate{

	private String id;
	@JsonIgnore
	private SecurityUser securityUser;

	public String getId() {
		return id;
	}

	public <T extends SecurityUserUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	public <T extends SecurityUserUpdate> T setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
		return (T) this;
	}
}
