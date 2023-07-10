package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.*;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.app.App;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.plugin.TestEntity;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.plugin.TestFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.plugin.TestInvoker;
import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import com.wizzdi.flexicore.security.response.PaginationResponse;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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
import java.util.Collections;
import java.util.stream.Collectors;

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

	private static final Logger logger = LoggerFactory.getLogger(PluginLoadingTest.class);

	private static final String pluginsPath;
	private static final String entitiesPath;
	private static final String PLUGIN_ID = "myPlugin";
	@Value("${flexicore.plugins}")
	private String pluginsDir;
	private DynamicExecution dynamicExecution;

	@Autowired
	private FlexiCorePluginManager flexiCorePluginManager;
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
					.extension("com.wizzdi.flexicore.boot.dynamic.invokers.service.plugin.TestEntity")
					.extension("com.wizzdi.flexicore.boot.dynamic.invokers.service.plugin.TestFilter")
					.extension("com.wizzdi.flexicore.boot.dynamic.invokers.service.plugin.TestInvoker")
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

	@BeforeAll
	public void init() {
		restTemplate.getRestTemplate().setInterceptors(
				Collections.singletonList((request, body, execution) -> {
					request.getHeaders()
							.add("authenticationKey", "fake");
					return execution.execute(request, body);
				}));

	}



	@Test
	@Order(1)
	public void getAllInvokers() {
		ParameterizedTypeReference<PaginationResponse<InvokerInfo>> ref= new ParameterizedTypeReference<>() {
		};
		ResponseEntity<PaginationResponse<InvokerInfo>> clazzResponse = this.restTemplate.exchange("/dynamicInvoker/getAll", HttpMethod.POST, new HttpEntity<>(new DynamicInvokerFilter()), ref);
		Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
		PaginationResponse<InvokerInfo> clazz = clazzResponse.getBody();
		Assertions.assertNotNull(clazz);
		logger.info("received: "+clazz.getList());

	}

	@Test
	@Order(2)
	public void createDynamicExecution() {
		DynamicExecutionCreate executeInvokerRequest=new DynamicExecutionCreate()
				.setMethodName("listTests")
				.setServiceCanonicalNames(Collections.singleton(TestInvoker.class.getCanonicalName()))
				.setExecutionParametersHolder(new TestFilter().setCurrentPage(0).setPageSize(10));
		ResponseEntity<DynamicExecution> clazzResponse = this.restTemplate.postForEntity("/dynamicExecution/create", executeInvokerRequest, DynamicExecution.class);
		Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
		dynamicExecution = clazzResponse.getBody();
		Assertions.assertNotNull(dynamicExecution);
		logger.info("received: "+dynamicExecution);

	}

	@Test
	@Order(3)
	public void listAllDynamicExecutions() {

		ParameterizedTypeReference<PaginationResponse<DynamicExecution>> ref= new ParameterizedTypeReference<>() {
		};
		ResponseEntity<PaginationResponse<DynamicExecution>> clazzResponse = this.restTemplate.exchange("/dynamicExecution/getAll", HttpMethod.POST, new HttpEntity<>(new DynamicInvokerFilter()), ref);
		Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
		PaginationResponse<DynamicExecution> clazz = clazzResponse.getBody();
		Assertions.assertNotNull(clazz);
		logger.info("received: "+clazz.getList());
		Assertions.assertTrue(clazz.getList().stream().anyMatch(f->f.getId().equals(dynamicExecution.getId())));


	}

	@Test
	@Order(4)
	public void getExample() {
		DynamicExecutionExampleRequest executeInvokerRequest=new DynamicExecutionExampleRequest()
				.setId(dynamicExecution.getId());
		ResponseEntity<Object> clazzResponse = this.restTemplate.postForEntity("/dynamicExecution/getDynamicExecutionReturnExample", executeInvokerRequest, Object.class);
		Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
		Object example = clazzResponse.getBody();
		Assertions.assertNotNull(example);
		logger.info("received: "+example);


	}

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Order(5)
	public void invokeListTests() throws JsonProcessingException {
		ExecuteInvokerRequest executeInvokerRequest=new ExecuteInvokerRequest()
				.setInvokerMethodName(dynamicExecution.getMethodName())
				.setInvokerNames(dynamicExecution.getServiceCanonicalNames().stream().map(f->f.getServiceCanonicalName()).collect(Collectors.toSet()))
				.setExecutionParametersHolder(dynamicExecution.getExecutionParametersHolder());
		ResponseEntity<ExecuteInvokersResponse> clazzResponse = this.restTemplate.postForEntity("/dynamicInvoker/executeInvoker", executeInvokerRequest, ExecuteInvokersResponse.class);
		Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
		ExecuteInvokersResponse clazz = clazzResponse.getBody();
		Assertions.assertNotNull(clazz);
		Assertions.assertNotNull(clazz.getResponses());
		Assertions.assertFalse(clazz.getResponses().isEmpty());
		logger.info("received: "+clazz);

	}

	@Test
	@Order(6)
	public void invokeListTestsDirect() throws JsonProcessingException {
		ParameterizedTypeReference<PaginationResponse<TestEntity>> ref= new ParameterizedTypeReference<>() {
		};
		ResponseEntity<PaginationResponse<TestEntity>> clazzResponse = this.restTemplate.exchange("/test/listTests", HttpMethod.POST, new HttpEntity<>(dynamicExecution.getExecutionParametersHolder()), ref);
		Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
		PaginationResponse<TestEntity> clazz = clazzResponse.getBody();
		Assertions.assertNotNull(clazz);
		logger.info("received: "+clazz.getList());

	}




}
