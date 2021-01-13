package com.flexicore.pluginA;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.RestServicePlugin;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/testEntity")
@RequestScoped
@Component
@Extension
public class PluginAREST implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private PluginAService pluginAService;

	@GET
	@Path("/test")
	public String test(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
		return pluginAService.test(name);
	}


}
