package com.wizzdi.flexicore.boot.jaxrs.interfaces;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;

@Produces({"application/json"})
@Consumes({"application/json"})
public interface RestServicePlugin extends Plugin {
}
