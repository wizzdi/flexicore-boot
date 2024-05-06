package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.TenantToUser;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;

public interface DefaultTenantToUserProvider<T extends TenantToUser> {

    T createTenantToUser(TenantToUserCreate tenantToUserCreate);

}
