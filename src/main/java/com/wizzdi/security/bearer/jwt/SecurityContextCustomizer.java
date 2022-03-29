package com.wizzdi.security.bearer.jwt;

import com.flexicore.security.SecurityContextBase;

public interface SecurityContextCustomizer {

    SecurityContextBase customize(SecurityContextBase securityContextBase);
}
