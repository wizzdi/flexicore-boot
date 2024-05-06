package com.wizzdi.security.bearer.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.crypto.SecretKey;

@Configuration
public class JwtParserConfig {


    @Value("${flexicore.security.jwt.secret:jwt-secret-for-our-application-ABCDEFGHJKL1234567890101112131415}")
    private String jwtTokenSecret;
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("cachedJWTSecret")
    public SecretKeyHolder cachedJWTSecret() {
        return new SecretKeyHolder(getJWTSecret());
    }

    @Bean
    public JwtParser jwtParser(SecretKeyHolder cachedJWTSecret) {
        return Jwts.parser().verifyWith(cachedJWTSecret.secretKey()).build();
    }

    private SecretKey getJWTSecret() {

            return Keys.hmacShaKeyFor(jwtTokenSecret.getBytes());




    }
}
