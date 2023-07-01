/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.flexicore.security.SecurityContext;

import java.util.List;


public class QueryInformationHolder<E extends Baseclass> {
	

	private Class<E> type;
	private List<String> batchFetchString; //hints for preventing n+1
	private SecurityContext securityContext;
	private FilteringInformationHolder filteringInformationHolder;


	public QueryInformationHolder(Class<E> type,SecurityContext securityContext) {
		this(new FilteringInformationHolder(),type,securityContext);
	}


	public QueryInformationHolder(FilteringInformationHolder filteringInformationHolder,Class<E> type,SecurityContext securityContext) {
		this.type = type;
		this.securityContext=securityContext;
		this.filteringInformationHolder=filteringInformationHolder;
	}


	public Class<E> getType() {
		return type;
	}

	public <T extends QueryInformationHolder<E>> T setType(Class<E> type) {
		this.type = type;
		return (T) this;
	}

	public List<String> getBatchFetchString() {
		return batchFetchString;
	}

	public <T extends QueryInformationHolder<E>> T setBatchFetchString(List<String> batchFetchString) {
		this.batchFetchString = batchFetchString;
		return (T) this;
	}

	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public <T extends QueryInformationHolder<E>> T setSecurityContext(SecurityContext securityContext) {
		this.securityContext = securityContext;
		return (T) this;
	}

	public FilteringInformationHolder getFilteringInformationHolder() {
		return filteringInformationHolder;
	}

	public <T extends QueryInformationHolder<E>> T setFilteringInformationHolder(FilteringInformationHolder filteringInformationHolder) {
		this.filteringInformationHolder = filteringInformationHolder;
		return (T) this;
	}
}
