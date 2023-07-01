package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;

public interface DefaultTenantProvider<T extends SecurityTenant> {

    T createDefaultTenant(SecurityTenantCreate securityTenantCreate);

}
