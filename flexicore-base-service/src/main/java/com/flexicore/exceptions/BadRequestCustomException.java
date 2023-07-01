package com.flexicore.exceptions;

import com.flexicore.interfaces.ErrorCodeException;

import jakarta.ws.rs.BadRequestException;

public class BadRequestCustomException extends BadRequestException implements ErrorCodeException {
    private int errorCode;

    public BadRequestCustomException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BadRequestCustomException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode=errorCode;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }
}
