package com.wizzdi.security.bearer.jwt;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenExtractor {

    String extractToken(HttpServletRequest httpServletRequest);
}
