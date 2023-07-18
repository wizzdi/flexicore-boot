package plugins;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.jpa.hibernate.app.TestEntity;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@Extension
public class PluginAController implements Plugin {

	private static final     Random random = new Random();

	@Autowired
	private PluginAService pluginAService;

	@PostMapping("/createTestEntity")
	public TestEntity createTestEntity(@RequestBody TestEntityCreate testEntityCreate) {

		return pluginAService.createTestEntity(testEntityCreate);
	}

	@GetMapping("/getTestEntity/{id}")
	public TestEntity getTestEntity(@PathVariable("id") String id) {
		return pluginAService.getTestEntity(id);
	}
}
