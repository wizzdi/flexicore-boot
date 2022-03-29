package com.wizzdi.security.bearer.jwt;

import com.flexicore.security.SecurityContextBase;
import io.jsonwebtoken.Claims;

public interface SecurityContextCustomizer {

    SecurityContextBase customize(SecurityContextBase securityContextBase, Claims claims);
}
