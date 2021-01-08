package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Role;

public class RoleToBaseclassCreate extends SecurityLinkCreate{

	@JsonIgnore
	private Role role;
	private String roleId;
	@JsonIgnore
	private Baseclass baseclass;
	private String baseclassId;

	@JsonIgnore
	public Role getRole() {
		return role;
	}

	public <T extends RoleToBaseclassCreate> T setRole(Role role) {
		this.role = role;
		return (T) this;
	}

	public String getRoleId() {
		return roleId;
	}

	public <T extends RoleToBaseclassCreate> T setRoleId(String roleId) {
		this.roleId = roleId;
		return (T) this;
	}

	@JsonIgnore
	public Baseclass getBaseclass() {
		return baseclass;
	}

	public <T extends RoleToBaseclassCreate> T setBaseclass(Baseclass baseclass) {
		this.baseclass = baseclass;
		return (T) this;
	}

	public String getBaseclassId() {
		return baseclassId;
	}

	public <T extends RoleToBaseclassCreate> T setBaseclassId(String baseclassId) {
		this.baseclassId = baseclassId;
		return (T) this;
	}
}
