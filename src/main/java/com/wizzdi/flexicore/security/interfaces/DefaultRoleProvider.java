package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.Role;
import com.wizzdi.flexicore.security.request.RoleCreate;

public interface DefaultRoleProvider<T extends Role> {

    T createRole(RoleCreate tenantToUserCreate);

}
