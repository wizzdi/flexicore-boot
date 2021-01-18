package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleToUserFilter extends BaselinkFilter {

	private Set<String> rolesIds=new HashSet<>();
	@JsonIgnore
	private List<Role> roles;
	private Set<String> usersIds=new HashSet<>();
	@JsonIgnore
	private List<SecurityUser> securityUsers;

	public Set<String> getRolesIds() {
		return rolesIds;
	}

	public <T extends RoleToUserFilter> T setRolesIds(Set<String> rolesIds) {
		this.rolesIds = rolesIds;
		return (T) this;
	}

	@JsonIgnore
	public List<Role> getRoles() {
		return roles;
	}

	public <T extends RoleToUserFilter> T setRoles(List<Role> roles) {
		this.roles = roles;
		return (T) this;
	}

	public Set<String> getUsersIds() {
		return usersIds;
	}

	public <T extends RoleToUserFilter> T setUsersIds(Set<String> usersIds) {
		this.usersIds = usersIds;
		return (T) this;
	}

	@JsonIgnore
	public List<SecurityUser> getSecurityUsers() {
		return securityUsers;
	}

	public <T extends RoleToUserFilter> T setSecurityUsers(List<SecurityUser> securityUsers) {
		this.securityUsers = securityUsers;
		return (T) this;
	}
}
