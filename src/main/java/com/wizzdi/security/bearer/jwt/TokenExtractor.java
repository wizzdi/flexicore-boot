package com.wizzdi.security.bearer.jwt;

import javax.servlet.http.HttpServletRequest;

public interface TokenExtractor {

    String extractToken(HttpServletRequest httpServletRequest);
}
