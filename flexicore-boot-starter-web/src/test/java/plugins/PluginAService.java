package plugins;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Extension
@RestController
public class PluginAService implements Plugin {

	@GetMapping("/test")
	public String createTestEntity(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
		return "hello "+name;
	}


}
