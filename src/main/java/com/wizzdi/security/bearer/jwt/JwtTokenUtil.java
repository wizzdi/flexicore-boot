package com.wizzdi.security.bearer.jwt;

import com.wizzdi.security.adapter.FlexicoreUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static java.lang.String.format;

@Component
public class JwtTokenUtil {
    private static final Logger logger= LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String ID = "ID";

    @Value("${flexicore.security.jwt.secretLocation:/home/flexicore/jwt.secret}")
    private String jwtTokenSecretLocation;
    @Value("${flexicore.security.jwt.secret:#{null}}")
    private String jwtTokenSecret;
    @Value("${flexicore.security.jwt.issuer:FlexiCore}")
    private String jwtIssuer;

    @Autowired
    @Qualifier("cachedJWTSecret")
    private SecretKey cachedJWTSecret;
    @Autowired
    private JwtParser jwtParser;


    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("cachedJWTSecret")
    public SecretKey cachedJWTSecret(){
        return getJWTSecret();
    }

    @Bean
    public JwtParser jwtParser (){
        return Jwts.parserBuilder().setSigningKey(cachedJWTSecret).build();
    }

    private SecretKey getJWTSecret() {
        if(cachedJWTSecret==null){
            if(jwtTokenSecret!=null){
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtTokenSecret));
            }
            File file=new File(jwtTokenSecretLocation);
            if(file.exists()){
                try {
                    String cachedJWTSecretStr = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    this.cachedJWTSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(cachedJWTSecretStr));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(cachedJWTSecret==null){
                cachedJWTSecret=  Keys.secretKeyFor(SignatureAlgorithm.HS512);

                try {
                    String secret=Encoders.BASE64.encode(cachedJWTSecret.getEncoded());
                    FileUtils.write(file,secret,StandardCharsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cachedJWTSecret;


    }



    public String generateAccessToken(FlexicoreUserDetails user) {

        String id = user.getId();
        return Jwts.builder()
                .setSubject(format("%s,%s", id, user.getUsername()))
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .claim(ID,id)
                .signWith(cachedJWTSecret)
                .compact();
    }

    public String getId(Jws<Claims> claimsJws) {
        return (String) claimsJws.getBody().get(ID);
    }


        public Jws<Claims> getClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token);
        }  catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return null;
    }

}
