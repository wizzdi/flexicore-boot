package com.flexicore.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RESTService;
import com.flexicore.response.HealthStatusResponse;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.stereotype.Component;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/health")
@RequestScoped
@ProtectedREST
@OperationsInside
@Tag(name = "Core")
@Tag(name = "Health")
@Component
@Extension
public class HealthRESTService implements RESTService {

    @Autowired
    private HealthEndpoint healthEndpoint;
            ;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "health", description = "health")
    public HealthStatusResponse healthCheck(
            @QueryParam("authenticationKey") String authenticationKey,
            @Context SecurityContext securityContext) {
        HealthComponent health = healthEndpoint.health();
        return new HealthStatusResponse(health);

    }



}
