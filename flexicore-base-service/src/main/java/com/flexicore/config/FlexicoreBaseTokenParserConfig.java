package com.flexicore.config;

import com.flexicore.model.Baseclass;
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
public class FlexicoreBaseTokenParserConfig {

    @Value("${flexicore.security.jwt.secretLocation:/home/flexicore/jwt.secret}")
    private String jwtTokenSecretLocation;
    @Value("${flexicore.security.jwt.secret:#{null}}")
    private String jwtTokenSecret;
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("cachedJWTSecret")
    public SecretKey cachedJWTSecret() {
        return getJWTSecret();
    }

    @Bean
    public JwtParser jwtParser(SecretKey cachedJWTSecret) {
        return Jwts.parserBuilder().setSigningKey(cachedJWTSecret).build();
    }



    private SecretKey getJWTSecret() {
        SecretKey cachedJWTSecret;
        if (jwtTokenSecret != null) {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtTokenSecret));
        }
        File file = new File(jwtTokenSecretLocation);
        if (file.exists()) {
            try {
                String cachedJWTSecretStr = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(cachedJWTSecretStr));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cachedJWTSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        try {
            String secret = Encoders.BASE64.encode(cachedJWTSecret.getEncoded());
            FileUtils.write(file, secret, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cachedJWTSecret;


    }

}
