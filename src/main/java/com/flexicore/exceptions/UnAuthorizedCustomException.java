package com.flexicore.exceptions;

import com.flexicore.interfaces.ErrorCodeException;
import org.jboss.resteasy.spi.HttpResponseCodes;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;

public class UnAuthorizedCustomException extends ClientErrorException  implements ErrorCodeException {

	private int errorCode;
	private static final int  status= HttpResponseCodes.SC_UNAUTHORIZED;

	public UnAuthorizedCustomException() {
		super(status);
	}

	public UnAuthorizedCustomException(String message) {
		super(message,status);
		// TODO Auto-generated constructor stub
	}

	public UnAuthorizedCustomException(Throwable cause) {
		super(status,cause);
		// TODO Auto-generated constructor stub
	}

	public UnAuthorizedCustomException(String message, Throwable cause) {
		super(message,status, cause);
		// TODO Auto-generated constructor stub
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
