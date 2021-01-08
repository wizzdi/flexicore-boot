package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.PermissionGroup;

public class PermissionGroupUpdate extends PermissionGroupCreate{

	private String id;
	@JsonIgnore
	private PermissionGroup permissionGroup;

	public String getId() {
		return id;
	}

	public <T extends PermissionGroupUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public PermissionGroup getPermissionGroup() {
		return permissionGroup;
	}

	public <T extends PermissionGroupUpdate> T setPermissionGroup(PermissionGroup permissionGroup) {
		this.permissionGroup = permissionGroup;
		return (T) this;
	}
}
