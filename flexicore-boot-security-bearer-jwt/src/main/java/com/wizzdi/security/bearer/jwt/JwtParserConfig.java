package com.wizzdi.security.bearer.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
