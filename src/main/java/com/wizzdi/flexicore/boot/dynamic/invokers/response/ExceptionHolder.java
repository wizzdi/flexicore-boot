package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;

public class ExceptionHolder {
    private final int status;
    private final String message;
    private final String reason;
    @JsonIgnore
    private final Throwable exception;


    public ExceptionHolder(Throwable exception){
        if(exception instanceof InvocationTargetException){
            InvocationTargetException invocationTargetException= (InvocationTargetException) exception;
            exception = invocationTargetException.getTargetException();
        }
        this.exception=exception;
        this.message=exception.getMessage();
        if(exception instanceof ResponseStatusException){
            ResponseStatusException responseStatusException= (ResponseStatusException) exception;
            this.status= responseStatusException.getRawStatusCode();
            this.reason=responseStatusException.getReason();
        }
        else{
            this.status=400;
            this.reason=null;
        }
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }

    @JsonIgnore
    public Throwable getException() {
        return exception;
    }
}
