/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.security.SecurityContext;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

@SuppressWarnings("serial")
@AnnotatedClazz(Category="core",  Name="ClazzLink",Description="Describes all other Link classes in the system")
//@Cache(type=CacheType.FULL)
@Entity

@XmlTransient
public class ClazzLink extends Clazz {



	public ClazzLink() {
	}

	public ClazzLink(String name, SecurityContext securityContext) {
		super(name, securityContext);
	}

	@ManyToOne(targetEntity = Clazz.class)
	private Clazz left;
	@ManyToOne(targetEntity = Clazz.class)

	private Clazz right;
	@ManyToOne(targetEntity = Clazz.class)

	private Clazz value;
	
	@ManyToOne(targetEntity=Clazz.class)

	public Clazz getLeft() {
		return left;
	}
	public void setLeft(Clazz left) {
		this.left = left;
	}
	@ManyToOne(targetEntity=Clazz.class)

	public Clazz getRight() {
		return right;
	}
	public void setRight(Clazz right) {
		this.right = right;
	}
	
	@ManyToOne(targetEntity=Clazz.class)

	public Clazz getValue() {
		return value;
	}
	public void setValue(Clazz value) {
		this.value = value;
	}
	
	
	
	

	
	
	
}
