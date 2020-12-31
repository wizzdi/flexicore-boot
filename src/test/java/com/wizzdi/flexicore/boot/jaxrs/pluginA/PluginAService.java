package com.wizzdi.flexicore.boot.jaxrs.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/testEntity")
@RequestScoped
@Component
@Extension
public class PluginAService implements Plugin {

	@GET
	@Path("/test")
	public String test(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
		return "hello "+name;
	}


}
