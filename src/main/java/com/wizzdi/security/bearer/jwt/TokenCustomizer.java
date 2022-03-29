package com.wizzdi.security.bearer.jwt;

import io.jsonwebtoken.JwtBuilder;

public interface TokenCustomizer {

    JwtBuilder customizeToken(JwtBuilder jwtBuilder);
}
