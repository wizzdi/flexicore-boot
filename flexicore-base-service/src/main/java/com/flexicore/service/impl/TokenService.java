package com.flexicore.service.impl;

import com.flexicore.config.SecretKeyHolder;
import com.flexicore.model.User;
import com.flexicore.response.JWTClaims;
import com.flexicore.response.impl.JWTClaimsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


@Primary
@Component
@Extension
public class TokenService implements com.flexicore.service.TokenService {




    @Autowired
    @Qualifier("cachedJWTSecret")
    private SecretKeyHolder cachedJWTSecret;
    @Autowired
    private JwtParser jwtParser;


    @Override
    public String getJwtToken(User user, OffsetDateTime expirationDate, String writeTenant, Set<String> readTenants, boolean totpVerified) {

        Map<String, Object> claims=new HashMap<>();
        if(writeTenant!=null){
            claims.put(WRITE_TENANT,writeTenant);
        }
        if(readTenants!=null && !readTenants.isEmpty()){
            claims.put(READ_TENANTS,readTenants);
        }
        if(user.isTotpEnabled()){
            claims.put(TOTP_VERIFIED,totpVerified);
        }
        return  Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expirationDate.toInstant()))
                .signWith(cachedJWTSecret.secretKey())
                .addClaims(claims)
                .compact();
    }


    @Override
    public String getJwtToken(User user, OffsetDateTime expirationDate){
        return getJwtToken(user,expirationDate,null,null);
    }
    @Override
    public String getJwtToken(User user, OffsetDateTime expirationDate, String writeTenant, Set<String> readTenants) {
        return getJwtToken(user, expirationDate, writeTenant, readTenants,false);
    }

    @Override
    public JWTClaims parseClaimsAndVerifyClaims(String jwtToken, Logger logger) {
        Claims claims =null;
        try {
            claims=jwtParser.parseClaimsJws(jwtToken).getBody();
        }
        catch (JwtException e){
            logger.log(Level.SEVERE,"invalid token ",e);
        }
        return claims!=null&&claims.getIssuer().equals(ISSUER)?new JWTClaimsImpl(claims):null;



    }

}
