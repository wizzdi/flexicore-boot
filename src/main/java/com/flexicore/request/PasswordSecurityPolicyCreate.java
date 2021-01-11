package com.flexicore.request;

import com.wizzdi.flexicore.security.request.SecurityPolicyCreate;

public class PasswordSecurityPolicyCreate extends SecurityPolicyCreate {

	private Integer minLength;
	private Boolean forceDigits;
	private Boolean forceLetters;
	private Boolean forceCapital;
	private Boolean forceLowerCase;

	public Integer getMinLength() {
		return minLength;
	}

	public <T extends PasswordSecurityPolicyCreate> T setMinLength(Integer minLength) {
		this.minLength = minLength;
		return (T) this;
	}

	public Boolean getForceDigits() {
		return forceDigits;
	}

	public <T extends PasswordSecurityPolicyCreate> T setForceDigits(Boolean forceDigits) {
		this.forceDigits = forceDigits;
		return (T) this;
	}

	public Boolean getForceLetters() {
		return forceLetters;
	}

	public <T extends PasswordSecurityPolicyCreate> T setForceLetters(Boolean forceLetters) {
		this.forceLetters = forceLetters;
		return (T) this;
	}

	public Boolean getForceCapital() {
		return forceCapital;
	}

	public <T extends PasswordSecurityPolicyCreate> T setForceCapital(Boolean forceCapital) {
		this.forceCapital = forceCapital;
		return (T) this;
	}

	public Boolean getForceLowerCase() {
		return forceLowerCase;
	}

	public <T extends PasswordSecurityPolicyCreate> T setForceLowerCase(Boolean forceLowerCase) {
		this.forceLowerCase = forceLowerCase;
		return (T) this;
	}
}
