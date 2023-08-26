package com.wizzdi.security.bearer.jwt;

import com.wizzdi.security.adapter.FlexicoreUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class FlexicoreJwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(FlexicoreJwtTokenUtil.class);
    private static final String ID = "ID";


    @Value("${flexicore.security.jwt.issuer:FlexiCore}")
    private String jwtIssuer;
    @Value("${flexicore.security.jwt.ttl:7d}")
    private Duration ttl;

    @Autowired
    @Qualifier("cachedJWTSecret")
    private SecretKeyHolder cachedJWTSecret;
    @Autowired
    private JwtParser jwtParser;





    public String generateAccessToken(FlexicoreUserDetails user) {
        return generateAccessToken(user, OffsetDateTime.now().plus(ttl));
    }

    public String generateAccessToken(FlexicoreUserDetails user, OffsetDateTime expirationDate){
        return generateAccessToken(user,expirationDate,Optional.empty());
    }


    public String generateAccessToken(FlexicoreUserDetails user, OffsetDateTime expirationDate, Optional<TokenCustomizer> tokenCustomizer) {

        String id = user.getId();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(format("%s,%s", id, user.getUsername()))
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expirationDate.toInstant())) // 1 week
                .claim(ID, id)
                .signWith(cachedJWTSecret.secretKey());
        JwtBuilder jwtBuilderCustomized=tokenCustomizer.map(f->f.customizeToken(jwtBuilder)).orElse(jwtBuilder);
        return jwtBuilderCustomized.compact();
    }

    public String getId(Jws<Claims> claimsJws) {
        return (String) claimsJws.getBody().get(ID);
    }


    public Jws<Claims> getClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token);
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty - {}", ex.getMessage());
        }
        catch (SignatureException e){
            logger.error("invalid signature",e);
        }
        catch (Exception ex){
            logger.error("failed parsing jwt token",ex);
        }
        return null;
    }

}
