package com.flexicore.exceptions;

public class ExceptionHolder {

    private int status;
    private int errorCode;
    private String message;

    public ExceptionHolder(int status, int errorCode, String message) {
        this.status=status;
        this.errorCode=errorCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
