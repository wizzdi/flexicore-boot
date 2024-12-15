package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.SecurityUser;
import com.wizzdi.segmantix.model.SecurityContext;

public interface SecurityContextProvider {

    SecurityContext getSecurityContext(SecurityUser user);
}
