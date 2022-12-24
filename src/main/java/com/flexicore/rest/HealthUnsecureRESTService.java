package com.flexicore.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.interfaces.RESTService;
import com.flexicore.response.HealthStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.core.MediaType;

@Path("/healthUnsecure")
@RequestScoped
@OperationsInside
@Tag(name = "Core")
@Tag(name = "healthUnsecure")
@Component
@Extension
public class HealthUnsecureRESTService implements RESTService {

    @Autowired
    private HealthEndpoint healthEndpoint;
    private HealthComponent healthComponent;
    @Value("${flexicore.health.minInterval:30000}")
    private long minInterval;
    private long time;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "health", description = "health")
    public HealthStatusResponse healthCheck() {
        return new HealthStatusResponse(getHealth());

    }

    private HealthComponent getHealth() {
        if(healthComponent==null||System.currentTimeMillis() - time >minInterval ){
            healthComponent= healthEndpoint.health();
            time=System.currentTimeMillis();
        }
        return healthComponent;
    }


    @GET
    @Path("healthOrFail")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "healthOrFail", description = "healthOrFail")
    public boolean healthOrFail() {
        boolean down=getHealth().getStatus()== Status.DOWN;
        if(down){
            throw new ServiceUnavailableException("Health check failed");
        }
       return true;
    }
}
