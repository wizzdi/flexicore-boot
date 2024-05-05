package com.wizzdi.flexicore.boot.dynamic.invokers.service.app;

import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@TestConfiguration
public class PluginConfig {


    private static final String pluginsPath;
    private static final String entitiesPath;
    private static final String PLUGIN_ID = "myPlugin";

    private static final Logger logger= LoggerFactory.getLogger(PluginConfig.class);




    static{
        pluginsPath= generatePluginDir("plugins");
        entitiesPath= generatePluginDir("entities");;
        try {
            File pluginsDir = new File(pluginsPath);
            if (!pluginsDir.exists()) {
                if (!pluginsDir.mkdirs()) {
                    logger.error("failed creating plugins dir");
                }
            }
            PluginJar pluginZip = new PluginJar.Builder(pluginsDir.toPath().resolve("my-plugin-1.2.3.jar"), PLUGIN_ID)
                    .extension("plugins.TestEntity")
                    .extension("plugins.TestFilter")
                    .extension("plugins.TestInvoker")
                    .pluginVersion("1.2.3")
                    .build();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }


    public static String getPluginsPath(){
        return pluginsPath;
    }
    public static String getEntitiesPath(){
        return entitiesPath;
    }

    private static String generatePluginDir(String prefix) {
        try {
            return Files.createTempDirectory(prefix).toFile().getAbsolutePath();

        }
        catch (Exception e){
            logger.error("failed getting "+prefix+" dir",e);
            return null;
        }

    }

}
