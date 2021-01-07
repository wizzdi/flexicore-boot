package com.flexicore.interfaces;


import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RestServicePlugin extends com.wizzdi.flexicore.boot.jaxrs.interfaces.RestServicePlugin {
}
