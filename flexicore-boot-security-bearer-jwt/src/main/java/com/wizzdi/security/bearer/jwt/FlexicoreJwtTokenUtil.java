package com.wizzdi.security.bearer.jwt;

import com.wizzdi.security.adapter.FlexicoreUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
                .subject(format("%s,%s", id, user.getUsername()))
                .issuer(jwtIssuer)
                .issuedAt(new Date())
                .expiration(Date.from(expirationDate.toInstant())) // 1 week
                .claim(ID, id)
                .signWith(cachedJWTSecret.secretKey());
        JwtBuilder jwtBuilderCustomized=tokenCustomizer.map(f->f.customizeToken(jwtBuilder)).orElse(jwtBuilder);
        return jwtBuilderCustomized.compact();
    }

    public String getId(Jws<Claims> claimsJws) {
        return (String) claimsJws.getPayload().get(ID);
    }


    public Jws<Claims> getClaims(String token) {
        try {
            return jwtParser.parseSignedClaims(token);
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
