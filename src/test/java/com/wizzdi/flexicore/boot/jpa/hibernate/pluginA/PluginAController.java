package com.wizzdi.flexicore.boot.jpa.hibernate.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.jpa.hibernate.app.TestEntity;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Extension
public class PluginAController implements Plugin {

	@Autowired
	private PluginAService pluginAService;

	@PostMapping("/createTestEntity")
	public TestEntity createTestEntity(@RequestParam(name = "name", required = false, defaultValue = "Stranger") String name) {
		return pluginAService.createTestEntity(name);
	}

	@GetMapping("/getTestEntity/{id}")
	public TestEntity getTestEntity(@PathVariable("id") String id) {
		return pluginAService.getTestEntity(id);
	}
}
