package com.flexicore.response;

import com.flexicore.exceptions.ExceptionHolder;

import java.util.Set;

public class PasswordSecurityPolicyErrorBody extends ExceptionHolder {

	private Set<PasswordPolicyError> errorSet;
	private Integer passwordRequiredLength;
	public PasswordSecurityPolicyErrorBody(int status, int errorCode, String message) {
		super(status, errorCode, message);
	}

	public Set<PasswordPolicyError> getErrorSet() {
		return errorSet;
	}

	public <T extends PasswordSecurityPolicyErrorBody> T setErrorSet(Set<PasswordPolicyError> errorSet) {
		this.errorSet = errorSet;
		return (T) this;
	}

	public Integer getPasswordRequiredLength() {
		return passwordRequiredLength;
	}

	public <T extends PasswordSecurityPolicyErrorBody> T setPasswordRequiredLength(Integer passwordRequiredLength) {
		this.passwordRequiredLength = passwordRequiredLength;
		return (T) this;
	}
}
