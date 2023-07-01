package com.flexicore.response;


import java.util.Date;
import java.util.Map;

public interface JWTClaims extends Map<String, Object> {
    String getIssuer();

    JWTClaims setIssuer(String iss);

    String getSubject();

    JWTClaims setSubject(String sub);

    String getAudience();

    JWTClaims setAudience(String aud);

    Date getExpiration();

    JWTClaims setExpiration(Date exp);

    Date getNotBefore();

    JWTClaims setNotBefore(Date nbf);

    Date getIssuedAt();

    JWTClaims setIssuedAt(Date iat);

    String getId();

    JWTClaims setId(String jti);

    <T> T get(String claimName, Class<T> requiredType);
}
