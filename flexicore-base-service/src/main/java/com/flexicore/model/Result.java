/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;

import java.io.Serializable;

@AnnotatedClazz(Category="Analyze", Name="AnalyzeResultInterface", Description="Used to track analyze result")

public class Result {

	private boolean succeeded;
	
	private String error;

	private Object result;
	
	public Result() {
		super();
	}

	public Result(boolean succeeded) {
		this.succeeded = succeeded;
	}

	public Result(boolean succeeded, Serializable result) {
		this.succeeded = succeeded;
		this.result = result;
	}

	public boolean isSucceeded() {
		return succeeded;
	}
	public void setSucceeded(boolean succeeded) {
		this.succeeded = succeeded;
	}
	
	public String getError() {
		return error;
	}
	public void setError(Exception error) {
		if(error!=null){
			if(this.error==null){
				this.error="";
			}
			this.error =this.error+","+ error.toString();
		}
		
	}

	public Object getResult() {
		return result;
	}


	public void setResult(Object result) {
		this.result = result;
	}



	@Override
	public String toString() {
		return "Result [succeeded=" + succeeded + ", " + (error != null ? "error=" + error + ", " : "")
				+ (result != null ? "result=" + result : "") + "]";
	}



	
	
	
	
	
	
	
	
	
	
}
