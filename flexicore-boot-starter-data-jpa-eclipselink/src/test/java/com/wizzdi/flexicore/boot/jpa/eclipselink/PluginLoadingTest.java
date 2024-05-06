package com.wizzdi.flexicore.boot.jpa.eclipselink;

import com.wizzdi.flexicore.boot.jpa.eclipselink.app.App;
import com.wizzdi.flexicore.boot.jpa.eclipselink.app.TestEntity;
import com.wizzdi.flexicore.boot.jpa.eclipselink.pluginA.PluginARepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class PluginLoadingTest {
	private final static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15")

			.withDatabaseName("flexicore-test")
			.withUsername("flexicore")
			.withPassword("flexicore");

	static{
		postgresqlContainer.start();
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresqlContainer::getUsername);
		registry.add("spring.datasource.password", postgresqlContainer::getPassword);
	}

	private static final Logger logger = LoggerFactory.getLogger(PluginLoadingTest.class);

	private static final String PLUGIN_ID = "myPlugin";
	@Value("${flexicore.plugins}")
	private String pluginsDir;

	@Autowired
	private PluginARepository pluginARepository;

	@Autowired
	private TestRestTemplate restTemplate;


	@Test
	public void testJpaPlugin() {
		ResponseEntity<TestEntity> createdTestEntityRequest = restTemplate.postForEntity("/createTestEntity",null, TestEntity.class);
		Assertions.assertEquals(200,createdTestEntityRequest.getStatusCode().value());
		TestEntity createdTestEntity = createdTestEntityRequest.getBody();
		Assertions.assertNotNull(createdTestEntity);
		logger.info("received "+createdTestEntity+" from plugin controller");
		ResponseEntity<TestEntity> fetchedTestEntityRequest = restTemplate.getForEntity("/getTestEntity/"+createdTestEntity.getId(), TestEntity.class);
		Assertions.assertEquals(200,fetchedTestEntityRequest.getStatusCode().value());
		TestEntity fetchedTestEntity = fetchedTestEntityRequest.getBody();

		Assertions.assertNotNull(fetchedTestEntity);
		logger.info("received "+fetchedTestEntity+" from plugin controller");


	}

}
