package com.flexicore.exceptions;

import com.flexicore.interfaces.ErrorCodeException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;

public class UnAuthorizedCustomException extends NotAuthorizedException implements ErrorCodeException {
	private int errorCode;
	public UnAuthorizedCustomException(Object challenge, Object... moreChallenges) {
		super(challenge, moreChallenges);
	}

	public UnAuthorizedCustomException(String message, Object challenge, Object... moreChallenges) {
		super(message, challenge, moreChallenges);
	}

	public UnAuthorizedCustomException(Response response) {
		super(response);
	}

	public UnAuthorizedCustomException(String message, Response response) {
		super(message, response);
	}

	public UnAuthorizedCustomException(Throwable cause, Object challenge, Object... moreChallenges) {
		super(cause, challenge, moreChallenges);
	}

	public UnAuthorizedCustomException(String message, Throwable cause, Object challenge, Object... moreChallenges) {
		super(message, cause, challenge, moreChallenges);
	}

	public UnAuthorizedCustomException(Response response, Throwable cause) {
		super(response, cause);
	}

	public UnAuthorizedCustomException(String message, Response response, Throwable cause) {
		super(message, response, cause);
	}

	public <T extends UnAuthorizedCustomException> T setErrorCode(int errorCode) {
		this.errorCode = errorCode;
		return (T) this;
	}

	@Override
	public int getErrorCode() {
		return errorCode;
	}

	public enum Errors{
		TOTP_REQUIRED,TOTP_LOCKED_OUT,TOTP_SETUP_REQUIRED
	}
}
