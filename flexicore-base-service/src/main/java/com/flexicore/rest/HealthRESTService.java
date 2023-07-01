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

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

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
