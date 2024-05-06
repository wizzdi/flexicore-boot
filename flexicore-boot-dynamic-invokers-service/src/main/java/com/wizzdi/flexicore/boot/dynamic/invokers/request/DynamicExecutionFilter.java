package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.PaginationFilter;

public class DynamicExecutionFilter extends PaginationFilter {


	private BasicPropertiesFilter basicPropertiesFilter;
	private DynamicInvokerMethodFilter dynamicInvokerMethodFilter;



	public BasicPropertiesFilter getBasicPropertiesFilter() {
		return basicPropertiesFilter;
	}

	public <T extends DynamicExecutionFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
		this.basicPropertiesFilter = basicPropertiesFilter;
		return (T) this;
	}

	public DynamicInvokerMethodFilter getDynamicInvokerMethodFilter() {
		return dynamicInvokerMethodFilter;
	}

	public <T extends DynamicExecutionFilter> T setDynamicInvokerMethodFilter(DynamicInvokerMethodFilter dynamicInvokerMethodFilter) {
		this.dynamicInvokerMethodFilter = dynamicInvokerMethodFilter;
		return (T) this;
	}
}
