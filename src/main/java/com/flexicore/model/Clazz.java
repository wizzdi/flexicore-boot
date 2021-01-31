/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.security.SecurityContextBase;

import javax.persistence.Entity;

@SuppressWarnings("serial")
@AnnotatedClazz(Category="core", Name="Clazz", Description="Describes all other classes in the system")
@Entity
@JsonSubTypes({
    @JsonSubTypes.Type(value=ClazzLink.class, name="ClazzLink")
})
public class Clazz extends Baseclass {

	public Clazz() {
	}

	public Clazz(String name, SecurityContextBase securityContext) {
		super(name, securityContext);
	}

}
