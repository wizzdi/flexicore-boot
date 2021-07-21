package com.wizzdi.flexicore.boot.jaxrs.interfaces;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@Produces({"application/json"})
@Consumes({"application/json"})
public interface RestServicePlugin extends Plugin {
}
