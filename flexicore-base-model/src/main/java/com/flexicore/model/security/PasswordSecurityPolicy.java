package com.flexicore.model.security;

import jakarta.persistence.Entity;

@Entity
public class PasswordSecurityPolicy extends SecurityPolicy{

	private int minLength;
	private boolean forceDigits;
	private boolean forceLetters;
	private boolean forceCapital;
	private boolean forceLowerCase;


	public int getMinLength() {
		return minLength;
	}

	public <T extends PasswordSecurityPolicy> T setMinLength(int minLength) {
		this.minLength = minLength;
		return (T) this;
	}

	public boolean isForceDigits() {
		return forceDigits;
	}

	public <T extends PasswordSecurityPolicy> T setForceDigits(boolean forceDigits) {
		this.forceDigits = forceDigits;
		return (T) this;
	}

	public boolean isForceLetters() {
		return forceLetters;
	}

	public <T extends PasswordSecurityPolicy> T setForceLetters(boolean forceLetters) {
		this.forceLetters = forceLetters;
		return (T) this;
	}

	public boolean isForceCapital() {
		return forceCapital;
	}

	public <T extends PasswordSecurityPolicy> T setForceCapital(boolean forceCapital) {
		this.forceCapital = forceCapital;
		return (T) this;
	}

	public boolean isForceLowerCase() {
		return forceLowerCase;
	}

	public <T extends PasswordSecurityPolicy> T setForceLowerCase(boolean forceLowerCase) {
		this.forceLowerCase = forceLowerCase;
		return (T) this;
	}
}
