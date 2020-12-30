/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.flexicore.boot.jaxrs.annotations;

public class Views {

	public Views() {
		// TODO Auto-generated constructor stub
	}

	  public static class Unrefined { }
	  public static class Elaborative extends Unrefined { }
	  public static class Full extends Elaborative { }
	  public static class ForSwaggerOnly extends Full{}

}
