package com.wizzdi.security.bearer.jwt;

import com.wizzdi.flexicore.security.configuration.SecurityContext;
import io.jsonwebtoken.Claims;

public interface SecurityContextCustomizer {

    SecurityContext customize(SecurityContext securityContext, Claims claims);
}
