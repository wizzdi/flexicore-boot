package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.configuration.SecurityContext;

public interface SecurityContextProvider {

    SecurityContext getSecurityContext(SecurityUser user);
}
