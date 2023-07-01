package com.flexicore.interceptors;

import com.flexicore.exceptions.ExceptionHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.ws.rs.WebApplicationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@Value("${flexicore.http.showExceptions:false}")
	private boolean showExceptionsInHttpResponse;
	@Value("${flexicore.http.exceptionPlaceHolder:Contact Your System Administrator}")
	private String exceptionPlaceHolder;

	@ExceptionHandler(value = {Exception.class})
	protected ResponseEntity<Object> handleExceptions(
			Exception ex, WebRequest request) {
		Object body=null;
		HttpStatus status=HttpStatus.INTERNAL_SERVER_ERROR;
		if(ex instanceof WebApplicationException){
			WebApplicationException webApplicationException = (WebApplicationException) ex;
			HttpStatus resolve = HttpStatus.resolve(webApplicationException.getResponse().getStatus());
			status=resolve!=null?resolve:HttpStatus.INTERNAL_SERVER_ERROR;
			body=webApplicationException.getResponse().getEntity();
		}
		if(ex instanceof ResponseStatusException){
			ResponseStatusException responseStatusException= (ResponseStatusException) ex;
			status= HttpStatus.valueOf(responseStatusException.getStatusCode().value());
			body=new ExceptionHolder(status.value(),-1,responseStatusException.getReason());
		}
		if(body==null){
			String message = showExceptionsInHttpResponse || status.is4xxClientError() ? ex.getMessage() : exceptionPlaceHolder;
			body=new ExceptionHolder(status.value(),-1,message);
		}
		if(status.equals(HttpStatus.UNAUTHORIZED)){
			logger.warn("UNAUTHORIZED",ex);
		}
		else{
			logger.error("exception:",ex);
		}
		return handleExceptionInternal(ex,body,new HttpHeaders(),status,request);
	}

}