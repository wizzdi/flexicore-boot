package com.wizzdi.flexicore.boot.base.exception;

public class ContextRefreshFailedException extends RuntimeException {
	public ContextRefreshFailedException() {
		super();
	}

	public ContextRefreshFailedException(String message) {
		super(message);
	}

	public ContextRefreshFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContextRefreshFailedException(Throwable cause) {
		super(cause);
	}

	protected ContextRefreshFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
