package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.RoleToBaseclass;

public class RoleToBaseclassUpdate extends RoleToBaseclassCreate{

	private String id;
	@JsonIgnore
	private RoleToBaseclass roleToBaseclass;

	public String getId() {
		return id;
	}

	public <T extends RoleToBaseclassUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public RoleToBaseclass getRoleToBaseclass() {
		return roleToBaseclass;
	}

	public <T extends RoleToBaseclassUpdate> T setRoleToBaseclass(RoleToBaseclass roleToBaseclass) {
		this.roleToBaseclass = roleToBaseclass;
		return (T) this;
	}
}
