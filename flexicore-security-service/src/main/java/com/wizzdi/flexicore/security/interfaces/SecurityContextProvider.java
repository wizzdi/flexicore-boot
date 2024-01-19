package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.SecurityUser;
import com.flexicore.security.SecurityContextBase;

public interface SecurityContextProvider {

    SecurityContextBase getSecurityContext(SecurityUser user);
}
