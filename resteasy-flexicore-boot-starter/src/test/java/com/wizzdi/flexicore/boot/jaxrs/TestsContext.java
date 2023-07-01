package com.wizzdi.flexicore.boot.jaxrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.nio.file.Files;

@ContextConfiguration
public class TestsContext {
	private static final Logger logger= LoggerFactory.getLogger(TestsContext.class);

	@DynamicPropertySource
	static void dynamicProperties(DynamicPropertyRegistry registry) throws IOException {

		registry.add("flexicore.plugins", ()->getPluginsDir("plugins"));
		registry.add("flexicore.entities", ()->getPluginsDir("entities"));

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
}
