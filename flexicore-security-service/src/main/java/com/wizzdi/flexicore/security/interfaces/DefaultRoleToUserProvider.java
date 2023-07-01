package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.Role;
import com.flexicore.model.RoleToUser;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.request.RoleToUserCreate;

public interface DefaultRoleToUserProvider<T extends RoleToUser> {

    T createRoleToUser(RoleToUserCreate roleToUserCreate);

}
