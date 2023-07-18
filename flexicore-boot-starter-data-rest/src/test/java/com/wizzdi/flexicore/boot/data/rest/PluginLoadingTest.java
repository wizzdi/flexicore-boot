package com.wizzdi.flexicore.boot.data.rest;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.data.rest.app.App;
import com.wizzdi.flexicore.boot.data.rest.app.Book;
import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@ExtendWith(SpringExtension.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

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

	private static final String pluginsPath;
	private static final String entitiesPath;
	private static final String PLUGIN_ID = "myPlugin";
	@Value("${flexicore.plugins}")
	private String pluginsDir;

	@Autowired
	private FlexiCorePluginManager flexiCorePluginManager;

	private Book book;

	@Autowired
	private TestRestTemplate restTemplate;

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
			PluginJar pluginZip = new PluginJar.Builder(pluginsDir.toPath().resolve("my-plugin-1.2.3.zip"), PLUGIN_ID)
					.extension("plugins.BookRepository")
					.extension("plugins.Config")
					.pluginVersion("1.2.3")
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


	@Test
	@Order(1)
	public void createBook() {
		ResponseEntity<Book> createdTestEntityRequest = restTemplate.postForEntity("/books",new Book().setName("test"), Book.class);
		Assertions.assertTrue(createdTestEntityRequest.getStatusCode().is2xxSuccessful());
		book = createdTestEntityRequest.getBody();
		Assertions.assertNotNull(book);
		logger.info("received "+book+" from book data rest");

	}

	@Test
	@Order(2)
	public void getBook() {
		ResponseEntity<Book> createdTestEntityRequest = restTemplate.getForEntity("/books/"+book.getId(), Book.class);
		Assertions.assertTrue(createdTestEntityRequest.getStatusCode().is2xxSuccessful());
		Book book = createdTestEntityRequest.getBody();
		Assertions.assertNotNull(book);
		logger.info("received "+book+" from book data rest");

	}

	@Test
	@Order(3)
	public void updateBook() {
		String newName = "test2";
		ResponseEntity<Book> createdTestEntityRequest = restTemplate.exchange("/books/"+book.getId(), HttpMethod.PUT,new HttpEntity<>(new Book().setName(newName),new HttpHeaders()), Book.class);
		Assertions.assertTrue(createdTestEntityRequest.getStatusCode().is2xxSuccessful());
		Book book = createdTestEntityRequest.getBody();
		Assertions.assertNotNull(book);
		logger.info("received "+book+" from book data rest");
		Assertions.assertEquals(newName,book.getName());


	}

}
