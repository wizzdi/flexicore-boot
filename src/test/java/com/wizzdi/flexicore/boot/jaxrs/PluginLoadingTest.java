package com.wizzdi.flexicore.boot.jaxrs;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.jaxrs.app.App;
import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;

@ExtendWith(SpringExtension.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")

public class PluginLoadingTest {

	private static final Logger logger= LoggerFactory.getLogger(PluginLoadingTest.class);

	@Value("${flexicore.plugins}")
	private String pluginsDir;

	@Autowired
	private FlexiCorePluginManager flexiCorePluginManager;

	@Autowired
	private TestRestTemplate restTemplate;

    @BeforeAll
    private void init() throws IOException {
        PluginJar pluginZip = new PluginJar.Builder(new File(pluginsDir).toPath().resolve("my-plugin-1.2.3.zip"), "myPlugin")
                .extension("com.wizzdi.flexicore.boot.jaxrs.pluginA.PluginAService")
                .pluginVersion("1.2.3")
                .build();

    }

	@Test
	public void testNoFailedPlugins() {
		ResponseEntity<String> test = restTemplate.getForEntity("/FlexiCore/rest/testEntity/test", String.class);
		Assertions.assertEquals(200,test.getStatusCodeValue());
		String body = test.getBody();
		Assertions.assertNotNull(body);
		logger.info("received "+body+" from plugin controller");

	}

}
