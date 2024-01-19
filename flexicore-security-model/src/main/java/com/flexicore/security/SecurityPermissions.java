package com.flexicore.security;

import com.flexicore.model.Role;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;

import java.util.List;

public record SecurityPermissions(SecurityPermissionEntry<SecurityUser> userPermissions, List<SecurityPermissionEntry<Role>> rolePermissions, List<SecurityPermissionEntry<SecurityTenant>> tenantPermissions) {

}
