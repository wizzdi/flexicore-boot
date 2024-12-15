package com.wizzdi.security.adapter;

import com.wizzdi.segmantix.model.SecurityContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;

public class FlexiCoreAuthentication extends UsernamePasswordAuthenticationToken {

    private final SecurityContext securityContext;

    public FlexiCoreAuthentication(FlexicoreUserDetails principal, SecurityContext securityContext) {
        super(principal, null, Collections.emptyList());
        this.securityContext = securityContext;
    }


    public SecurityContext getSecurityContextBase() {
        return securityContext;
    }
}
