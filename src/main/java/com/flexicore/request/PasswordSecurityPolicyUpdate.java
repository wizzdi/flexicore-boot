package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.security.PasswordSecurityPolicy;

public class PasswordSecurityPolicyUpdate extends PasswordSecurityPolicyCreate {

	private String id;
	@JsonIgnore
	private PasswordSecurityPolicy passwordSecurityPolicy;

	public String getId() {
		return id;
	}

	public <T extends PasswordSecurityPolicyUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public PasswordSecurityPolicy getPasswordSecurityPolicy() {
		return passwordSecurityPolicy;
	}

	public <T extends PasswordSecurityPolicyUpdate> T setPasswordSecurityPolicy(PasswordSecurityPolicy passwordSecurityPolicy) {
		this.passwordSecurityPolicy = passwordSecurityPolicy;
		return (T) this;
	}
}
