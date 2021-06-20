package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;

public interface DefaultUserProvider<T extends SecurityUser> {

    T createSecurityUser(SecurityUserCreate securityUserCreate);

}
