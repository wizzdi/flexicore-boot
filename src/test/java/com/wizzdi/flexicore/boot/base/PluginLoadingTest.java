package com.wizzdi.flexicore.boot.base;

import com.wizzdi.flexicore.boot.base.app.App;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")

public class PluginLoadingTest {

	private static final Logger logger= LoggerFactory.getLogger(PluginLoadingTest.class);
	private static final String pluginsPath;
	private static final String entitiesPath;
	private static final String PLUGIN_A_ID = "plugin-a";
	private static final String PLUGIN_B_ID = "plugin-b";

	@Value("${flexicore.plugins}")
	private String pluginsDir;

	@Autowired
	private FlexiCorePluginManager flexiCorePluginManager;

	static{
		pluginsPath=getPluginsDir("plugins");
		entitiesPath=getPluginsDir("entities");;
		try {
			File pluginsDir = new File(pluginsPath);
			if (!pluginsDir.exists()) {
				if (!pluginsDir.mkdirs()) {
					logger.error("failed creating plugins dir");
				}
			}
			PluginJar pluginAZip = new PluginJar.Builder(pluginsDir.toPath().resolve("plugin-A-1.0.0.zip"), PLUGIN_A_ID)
					.extension("com.wizzdi.flexicore.boot.base.pluginA.PluginAService")
					.extension("com.wizzdi.flexicore.boot.base.pluginA.SomeInterface")
					.extension("com.wizzdi.flexicore.boot.base.pluginA.SomeInterfaceUser")
					.pluginVersion("1.0.0")
					.build();

			PluginJar pluginBZip = new PluginJar.Builder(pluginsDir.toPath().resolve("plugin-B-1.0.0.zip"), PLUGIN_B_ID)
					.extension("com.wizzdi.flexicore.boot.base.pluginB.PluginBService")
					.pluginVersion("1.0.0")
					.manifestAttribute("Plugin-Dependencies",PLUGIN_A_ID+"@>=1.0.0")
					.build();

		}
		catch (Exception e){
			throw new RuntimeException(e);
		}

	}

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) throws IOException {

		registry.add("flexicore.plugins", ()->pluginsPath);
		registry.add("flexicore.entities", ()->entitiesPath);

	}

	private static String getPluginsDir(String prefix) {
		try {
			return Files.createTempDirectory(prefix).toFile().getAbsolutePath();

		}
		catch (Exception e){
			logger.error("failed getting "+prefix+" dir",e);
			return null;
		}

	}

	@AfterAll
	private void finish(){
		File pluginsDir=new File(this.pluginsDir);
		if(!pluginsDir.delete()){
			logger.error("failed deleting plugins dir");
		}
	}

	@Test
	public void testNoFailedPlugins() {

		Set<String> started = flexiCorePluginManager.getStartedPlugins().stream().map(f -> f.getPluginId()).collect(Collectors.toSet());
		Assert.assertTrue(started.contains(PLUGIN_A_ID));

	}

}
