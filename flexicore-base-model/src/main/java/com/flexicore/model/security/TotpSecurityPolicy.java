package com.flexicore.model.security;

import jakarta.persistence.Entity;

@Entity
public class TotpSecurityPolicy extends SecurityPolicy{

	private boolean forceTotp;
	private long allowedConfigureOffsetMs;


	public boolean isForceTotp() {
		return forceTotp;
	}

	public <T extends TotpSecurityPolicy> T setForceTotp(boolean forceTotp) {
		this.forceTotp = forceTotp;
		return (T) this;
	}

	public long getAllowedConfigureOffsetMs() {
		return allowedConfigureOffsetMs;
	}

	public <T extends TotpSecurityPolicy> T setAllowedConfigureOffsetMs(long allowedConfigureOffsetMs) {
		this.allowedConfigureOffsetMs = allowedConfigureOffsetMs;
		return (T) this;
	}
}
