package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.PermissionGroupToBaseclass;

public class PermissionGroupToBaseclassUpdate extends PermissionGroupToBaseclassCreate{

	private String id;
	@JsonIgnore
	private PermissionGroupToBaseclass permissionGroupToBaseclass;

	public String getId() {
		return id;
	}

	public <T extends PermissionGroupToBaseclassUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public PermissionGroupToBaseclass getPermissionGroupToBaseclass() {
		return permissionGroupToBaseclass;
	}

	public <T extends PermissionGroupToBaseclassUpdate> T setPermissionGroupToBaseclass(PermissionGroupToBaseclass permissionGroupToBaseclass) {
		this.permissionGroupToBaseclass = permissionGroupToBaseclass;
		return (T) this;
	}
}
