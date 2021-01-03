package com.flexicore.service;

import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.User;
import com.flexicore.response.JWTClaims;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.logging.Logger;

public interface TokenService extends FlexiCoreService {
    String ISSUER = "FlexiCore";
    String WRITE_TENANT="WRITE_TENANT";
    String READ_TENANTS="READ_TENANT";
    String TOTP_VERIFIED = "TOTP_VERIFIED";


    /**
     * returns a JWT token for the user that will expire at @expirationDate
     * @param user user to get the JWT token for
     * @param expirationDate datetime at which the token will expire
     * @return jwt token
     */
    String getJwtToken(User user, OffsetDateTime expirationDate);

    /**
     * returns a JWT token for the user that will expire at @expirationDate , with a specific write tenant and readTenants
     * @param user  user to get the JWT token for
     * @param expirationDate datetime at which the token will expire
     * @param writeTenant tenant that will be used when the user creates objects
     * @param readTenants tenants that the result set of request will be contained to
     * @return
     */
    String getJwtToken(User user, OffsetDateTime expirationDate, String writeTenant, Set<String> readTenants);

    /**
     * returns a JWT token for the user that will expire at @expirationDate , with a specific write tenant and readTenants
     * @param user  user to get the JWT token for
     * @param expirationDate datetime at which the token will expire
     * @param writeTenant tenant that will be used when the user creates objects
     * @param readTenants tenants that the result set of request will be contained to
     * @param totpVerified true/false if token should be generated post totp verification
     * @return
     */
    String getJwtToken(User user, OffsetDateTime expirationDate, String writeTenant, Set<String> readTenants,boolean totpVerified);

    /**
     * parses jwt token into claims
     * @param jwtToken
     * @param logger
     * @return
     */
    JWTClaims parseClaimsAndVerifyClaims(String jwtToken, Logger logger);
}
