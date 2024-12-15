package com.wizzdi.security.bearer.jwt;

import com.wizzdi.segmantix.model.SecurityContext;
import io.jsonwebtoken.Claims;

public interface SecurityContextCustomizer {

    SecurityContext customize(SecurityContext securityContext, Claims claims);
}
