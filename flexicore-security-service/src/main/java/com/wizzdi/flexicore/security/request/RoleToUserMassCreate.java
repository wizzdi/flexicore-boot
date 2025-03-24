package com.wizzdi.flexicore.security.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RoleToUserMassCreate {
	@NotEmpty
	@NotNull
	@Valid
	private List<RoleToUserCreate> roleToUserCreates;

	public List<RoleToUserCreate> getRoleToUserCreates() {
		return roleToUserCreates;
	}

	public <T extends RoleToUserMassCreate> T setRoleToUserCreates(List<RoleToUserCreate> roleToUserCreates) {
		this.roleToUserCreates = roleToUserCreates;
		return (T) this;
	}
}
