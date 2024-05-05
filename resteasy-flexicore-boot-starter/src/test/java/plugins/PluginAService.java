package plugins;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.jaxrs.interfaces.RestServicePlugin;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/testEntity")
@Component
@Extension
public class PluginAService implements RestServicePlugin {

	@GET
	@Path("/test")
	public String test(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
		return "hello "+name;
	}


}
