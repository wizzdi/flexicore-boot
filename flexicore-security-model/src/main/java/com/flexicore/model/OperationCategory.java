/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.security.SecurityContextBase;


import jakarta.persistence.Entity;

/**
 * 
 * @author avishayb
 *
 */
@AnnotatedClazz(Category="access control", Name="Operation Category", Description="Clasifies operations")
@Entity



public class OperationCategory extends Basic {

	public OperationCategory() {
		super();
	}


}
