/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.exceptions;

import org.jboss.resteasy.spi.HttpResponseCodes;

import javax.ws.rs.ClientErrorException;

public class UserCannotBeRegisteredException extends ClientErrorException {
	private static final int  status=HttpResponseCodes.SC_BAD_REQUEST;

	public UserCannotBeRegisteredException() {
		super(status);
	}

	public UserCannotBeRegisteredException(String message) {
		super(message,status);
		// TODO Auto-generated constructor stub
	}

	public UserCannotBeRegisteredException(Throwable cause) {
		super(status,cause);
		// TODO Auto-generated constructor stub
	}

	public UserCannotBeRegisteredException(String message, Throwable cause) {
		super(message,status, cause);
		// TODO Auto-generated constructor stub
	}

	

}
