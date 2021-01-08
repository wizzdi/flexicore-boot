package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Tenant;

public class TenantToBaseclassPermissionCreate extends SecurityLinkCreate{

	@JsonIgnore
	private Baseclass baseclass;
	private  String baseclassId;


	@JsonIgnore
	public Baseclass getBaseclass() {
		return baseclass;
	}

	public <T extends TenantToBaseclassPermissionCreate> T setBaseclass(Baseclass baseclass) {
		this.baseclass = baseclass;
		return (T) this;
	}

	public String getBaseclassId() {
		return baseclassId;
	}

	public <T extends TenantToBaseclassPermissionCreate> T setBaseclassId(String baseclassId) {
		this.baseclassId = baseclassId;
		return (T) this;
	}
}
