package com.wizzdi.security.adapter;

import com.flexicore.security.SecurityContextBase;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;

public class FlexiCoreAuthentication extends UsernamePasswordAuthenticationToken {

    private final SecurityContextBase securityContextBase;

    public FlexiCoreAuthentication(FlexicoreUserDetails principal,SecurityContextBase securityContextBase) {
        super(principal, null, Collections.emptyList());
        this.securityContextBase=securityContextBase;
    }


    public SecurityContextBase getSecurityContextBase() {
        return securityContextBase;
    }
}
