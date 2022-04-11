/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.rest;

import com.flexicore.annotations.Audit;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.request.AuthenticationRequest;
import com.flexicore.response.AuthenticationResponse;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.impl.SecurityService;
import com.flexicore.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * no need to intercept for security here.
 *
 * @author Avishay Ben Natan
 */
@RequestScoped
@Component
@Extension
@OperationsInside
@Tag(name = "AuthenticationNew")
@Path("/authenticationNew")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationNewRESTService implements RestServicePlugin {

    @Autowired
    private UserService userservice;

    @Autowired
    @Lazy
    private SecurityService securityService;

    /**
     * Login into the system, if successful, receives an authentication key for
     * further operations.
     *
     * @param authenticationRequest authentication request
     * @return authentication response
     */
    @POST
    @Path("/login")
    @Operation(summary = "login", description = "Login to the system")
    @Audit(auditType = "Login")
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        SecurityContext securityContext=securityService.getAdminUserSecurityContext();
       userservice.validate(authenticationRequest,securityContext);
       return userservice.login(authenticationRequest,securityContext);

    }






}
