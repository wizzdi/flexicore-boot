package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.security.TotpSecurityPolicy;

public class TotpSecurityPolicyUpdate extends TotpSecurityPolicyCreate {

	private String id;
	@JsonIgnore
	private TotpSecurityPolicy totpSecurityPolicy;

	public String getId() {
		return id;
	}

	public <T extends TotpSecurityPolicyUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public TotpSecurityPolicy getTotpSecurityPolicy() {
		return totpSecurityPolicy;
	}

	public <T extends TotpSecurityPolicyUpdate> T setTotpSecurityPolicy(TotpSecurityPolicy totpSecurityPolicy) {
		this.totpSecurityPolicy = totpSecurityPolicy;
		return (T) this;
	}
}
