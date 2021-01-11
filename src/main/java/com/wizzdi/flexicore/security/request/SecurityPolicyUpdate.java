package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.security.SecurityPolicy;

public class SecurityPolicyUpdate extends SecurityPolicyCreate{

	private String id;
	@JsonIgnore
	private SecurityPolicy securityPolicy;

	public String getId() {
		return id;
	}

	public <T extends SecurityPolicyUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public SecurityPolicy getSecurityPolicy() {
		return securityPolicy;
	}

	public <T extends SecurityPolicyUpdate> T setSecurityPolicy(SecurityPolicy SecurityPolicy) {
		this.securityPolicy = SecurityPolicy;
		return (T) this;
	}
}
