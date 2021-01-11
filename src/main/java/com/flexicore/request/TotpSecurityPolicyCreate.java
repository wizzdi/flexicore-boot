package com.flexicore.request;

import com.wizzdi.flexicore.security.request.SecurityPolicyCreate;

public class TotpSecurityPolicyCreate extends SecurityPolicyCreate {

	private Boolean forceTotp;
	private Long allowedConfigureOffsetMs;


	public Boolean getForceTotp() {
		return forceTotp;
	}

	public <T extends TotpSecurityPolicyCreate> T setForceTotp(Boolean forceTotp) {
		this.forceTotp = forceTotp;
		return (T) this;
	}

	public Long getAllowedConfigureOffsetMs() {
		return allowedConfigureOffsetMs;
	}

	public <T extends TotpSecurityPolicyCreate> T setAllowedConfigureOffsetMs(Long allowedConfigureOffsetMs) {
		this.allowedConfigureOffsetMs = allowedConfigureOffsetMs;
		return (T) this;
	}
}
