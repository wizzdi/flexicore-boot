/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.security;



import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.model.Operation;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;

import java.util.List;


@AnnotatedClazz(Category="SecurityContext", Name="SecurityContext", Description="holds Context Information")
public class SecurityContext {


	private List<Tenant> tenants;
	private User user;
	private Operation operation;
	private boolean impersonated;

	private Tenant tenantToCreateIn;

	public SecurityContext(List<Tenant> tenants, User user, Operation operation, Tenant tenantToCreateIn) {
		this.tenants = tenants;
		this.user = user;
		this.operation = operation;
		this.tenantToCreateIn = tenantToCreateIn;
	}

	public SecurityContext() {
		// TODO Auto-generated constructor stub
	}




	public List<Tenant> getTenants() {
		return tenants;
	}



	public User getUser() {
		return user;
	}



	public Operation getOperation() {
		return operation;
	}



	public Tenant getTenantToCreateIn() {
		return tenantToCreateIn;
	}

	public <T extends SecurityContext> T setTenants(List<Tenant> tenants) {
		this.tenants = tenants;
		return (T) this;
	}

	public <T extends SecurityContext> T setUser(User user) {
		this.user = user;
		return (T) this;
	}

	public <T extends SecurityContext> T setOperation(Operation operation) {
		this.operation = operation;
		return (T) this;
	}

	public <T extends SecurityContext> T setTenantToCreateIn(Tenant tenantToCreateIn) {
		this.tenantToCreateIn = tenantToCreateIn;
		return (T) this;
	}

	public boolean isImpersonated() {
		return impersonated;
	}

	public <T extends SecurityContext> T setImpersonated(boolean impersonated) {
		this.impersonated = impersonated;
		return (T) this;
	}
}
