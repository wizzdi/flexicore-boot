package com.wizzdi.security.adapter;

import com.wizzdi.flexicore.security.configuration.SecurityContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;

public class FlexiCoreAuthentication extends UsernamePasswordAuthenticationToken {

    private final SecurityContext securityContext;

    public FlexiCoreAuthentication(FlexicoreUserDetails principal, SecurityContext securityContext) {
        super(principal, null, Collections.emptyList());
        this.securityContext = securityContext;
    }


    public SecurityContext getSecurityContext() {
        return securityContext;
    }
}
