package com.wizzdi.flexicore.boot.base;

import com.wizzdi.flexicore.boot.base.app.App;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")

public class PluginLoadingTest {


	private static final String PLUGIN_ID = "myPlugin";
	@Value("${flexicore.plugins}")
	private String pluginsDir;

	@Autowired
	private FlexiCorePluginManager flexiCorePluginManager;

    @BeforeAll
    private void init() throws IOException {
        PluginJar pluginZip = new PluginJar.Builder(new File(pluginsDir).toPath().resolve("my-plugin-1.2.3.zip"), PLUGIN_ID)
                .extension("com.wizzdi.flexicore.boot.base.pluginA.PluginAService")
                .pluginVersion("1.2.3")
                .build();

    }

	@Test
	public void testNoFailedPlugins() {

		Set<String> started = flexiCorePluginManager.getStartedPlugins().stream().map(f -> f.getPluginId()).collect(Collectors.toSet());
		Assert.assertTrue(started.contains(PLUGIN_ID));

	}

}
