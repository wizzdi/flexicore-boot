package com.wizzdi.flexicore.boot.jaxrs.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/testEntity")
@Component
@Extension
public class PluginAService implements Plugin {

	@GET
	@Path("/test")
	public String test(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
		return "hello "+name;
	}


}
