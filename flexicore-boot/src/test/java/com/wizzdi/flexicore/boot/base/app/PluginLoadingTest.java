package com.wizzdi.flexicore.boot.base.app;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
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
	private static final String PLUGIN_C_ID = "plugin-c";
	private static final String PLUGIN_D_ID = "plugin-d";
	private static final String PLUGIN_E_ID = "plugin-e";
	private static final String PLUGIN_F_ID = "plugin-f";


	@Value("${flexicore.plugins}")
	private String pluginsDir;

	@Autowired
	private FlexiCorePluginManager flexiCorePluginManager;
	@Autowired
	private ApplicationContext applicationContext;

	static{
		pluginsPath=getPluginsDir("plugins");
		entitiesPath=getPluginsDir("entities");
		try {
			File pluginsDir = new File(pluginsPath);
			if (!pluginsDir.exists()) {
				if (!pluginsDir.mkdirs()) {
					logger.error("failed creating plugins dir");
				}
			}
			PluginJar pluginAZip = new PluginJar.Builder(pluginsDir.toPath().resolve("plugin-A-1.0.0.jar"), PLUGIN_A_ID)
					.extension("plugins.test.pluginA.PluginAService")
					.extension("plugins.test.pluginA.SomeInterface")
					.extension("plugins.test.pluginA.SomeInterfaceUser")
					.pluginVersion("1.0.0")
					.build();

			PluginJar pluginBZip = new PluginJar.Builder(pluginsDir.toPath().resolve("plugin-B-1.0.0.jar"), PLUGIN_B_ID)
					.extension("plugins.test.pluginB.PluginBService")
					.pluginVersion("1.0.0")
					.manifestAttribute("Plugin-Dependencies",PLUGIN_A_ID+"@>=1.0.0")
					.build();

			PluginJar pluginCZip = new PluginJar.Builder(pluginsDir.toPath().resolve("plugin-C-1.0.0.jar"), PLUGIN_C_ID)
					.extension("plugins.test.pluginC.PluginCService")
					.pluginVersion("1.0.0")
					.manifestAttribute("Plugin-Dependencies",PLUGIN_B_ID+"@>=1.0.0,"+PLUGIN_A_ID+"@>=1.0.0")
					.build();
			PluginJar pluginDZip = new PluginJar.Builder(pluginsDir.toPath().resolve("plugin-D-1.0.0.jar"), PLUGIN_D_ID)
					.extension("plugins.test.pluginD.PluginDService")
					.pluginVersion("1.0.0")
					.manifestAttribute("Plugin-Dependencies",PLUGIN_A_ID+"@>=1.0.0")
					.build();

			PluginJar pluginEZip = new PluginJar.Builder(pluginsDir.toPath().resolve("plugin-E-1.0.0.jar"), PLUGIN_E_ID)
					.extension("plugins.test.pluginE.PluginEService")
					.pluginVersion("1.0.0")
					.manifestAttribute("Plugin-Dependencies",PLUGIN_D_ID+"@>=1.0.0,"+PLUGIN_A_ID+"@>=1.0.0,"+PLUGIN_C_ID+"@>=1.0.0,"+PLUGIN_B_ID+"@>=1.0.0")
					.build();
			PluginJar pluginFZip = new PluginJar.Builder(pluginsDir.toPath().resolve("plugin-F-1.0.0.jar"), PLUGIN_F_ID)
					.extension("plugins.test.pluginF.PluginFService")
					.pluginVersion("1.0.0")
					.manifestAttribute("Plugin-Dependencies",PLUGIN_D_ID+"@>=1.0.0,"+PLUGIN_A_ID+"@>=1.0.0,"+PLUGIN_C_ID+"@>=1.0.0,"+PLUGIN_B_ID+"@>=1.0.0,"+PLUGIN_E_ID+"@>=1.0.0")
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
	public void finish(){
		File pluginsDir=new File(this.pluginsDir);
		if(!pluginsDir.delete()){
			logger.error("failed deleting plugins dir");
		}
	}


	@Test
	public void testNoFailedPlugins() {

		Set<String> started = flexiCorePluginManager.getStartedPlugins().stream().map(f -> f.getPluginId()).collect(Collectors.toSet());
		Assertions.assertTrue(started.contains(PLUGIN_A_ID));

		Map<String,List<Object>> collect = flexiCorePluginManager.getStartedPlugins().stream().map(f -> f.getPluginId()).collect(Collectors.toMap(f->f, f->flexiCorePluginManager.getExtensions(f)));
		System.out.println("loaded objects:"+collect);

	}

}
