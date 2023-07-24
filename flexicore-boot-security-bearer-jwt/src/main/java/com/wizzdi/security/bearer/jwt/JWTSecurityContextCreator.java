package com.wizzdi.security.bearer.jwt;

import com.flexicore.model.SecurityUser;
import com.flexicore.model.SecurityUser_;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.interfaces.SecurityContextProvider;
import com.wizzdi.flexicore.security.service.SecurityUserService;
import com.wizzdi.security.adapter.FlexiCoreAuthentication;
import com.wizzdi.security.adapter.FlexicoreUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class JWTSecurityContextCreator {
    @Autowired
    private FlexicoreJwtTokenUtil flexicoreJwtTokenUtil;
    @Autowired
    @Lazy
    private SecurityUserService securityUserService;
    @Autowired
    @Lazy
    private   SecurityContextProvider securityContextProvider;
    @Autowired
    private ObjectProvider<SecurityContextCustomizer> securityContextCustomizers;

    public FlexiCoreAuthentication getSecurityContext(String token){
        Jws<Claims> claimsJws = flexicoreJwtTokenUtil.getClaims(token);
        if (claimsJws==null) {

            return null;
        }
        Claims claims = claimsJws.getBody();
        // Get user identity and set it on the spring security context
        String id = flexicoreJwtTokenUtil.getId(claimsJws);
        SecurityUser securityUser = securityUserService.getByIdOrNull(id, SecurityUser.class, SecurityUser_.security,null);
        FlexicoreUserDetails userDetails = getUserDetails(securityUser);
        SecurityContextBase securityContext = securityContextProvider.getSecurityContext(securityUser);
        for (SecurityContextCustomizer securityContextCustomizer : securityContextCustomizers.orderedStream().toList()) {
            securityContext=securityContextCustomizer.customize(securityContext,claims);
        }
        return new FlexiCoreAuthentication(userDetails, securityContext);
    }

    private FlexicoreUserDetails getUserDetails(SecurityUser securityUser) {
        return new FlexicoreUserDetails(securityUser.getId(),null, null);
    }
}
