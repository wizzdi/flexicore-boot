package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.*;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.app.App;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.app.PluginConfig;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class DynamicInvokerTests {

    private static final Logger logger= LoggerFactory.getLogger(DynamicInvokerTests.class);
	    private final static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15")

			.withDatabaseName("flexicore-test")
			.withUsername("flexicore")
			.withPassword("flexicore");

	static{
		postgresqlContainer.start();
	}
    @Autowired
    private TestRestTemplate restTemplate;

    private DynamicExecution dynamicExecution;



    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }


    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) throws IOException {

        registry.add("flexicore.plugins", PluginConfig::getPluginsPath);
        registry.add("flexicore.entities", PluginConfig::getEntitiesPath);

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
    @Order(2)
    public void testListAllDynamicInvokers() {
        DynamicInvokerFilter request=new DynamicInvokerFilter()
                .setBasicPropertiesFilter(new BasicPropertiesFilter().setNameLike("Test"))
                .setMethodNameLike("listTests");
        ParameterizedTypeReference<PaginationResponse<Map<String,Object>>> t= new ParameterizedTypeReference<>() {};

        ResponseEntity<PaginationResponse<Map<String,Object>>> dynamicInvokerResponse = this.restTemplate.exchange("/dynamicInvoker/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, dynamicInvokerResponse.getStatusCode().value());
        PaginationResponse<Map<String,Object>> body = dynamicInvokerResponse.getBody();
        Assertions.assertNotNull(body);
        List<Map<String,Object>> dynamicInvokers = body.getList();
        Assertions.assertNotEquals(0,dynamicInvokers.size());
        System.out.println("received "+dynamicInvokers.size() +" invokers: "+dynamicInvokers);


    }

    @Test
    @Order(2)
    public void testListAllDynamicInvokerHolders() {
        DynamicInvokerFilter request=new DynamicInvokerFilter()
                .setBasicPropertiesFilter(new BasicPropertiesFilter().setNameLike("Test"))
                .setMethodNameLike("listTests");
        ParameterizedTypeReference<PaginationResponse<Map<String,Object>>> t= new ParameterizedTypeReference<>() {};

        ResponseEntity<PaginationResponse<Map<String,Object>>> dynamicInvokerResponse = this.restTemplate.exchange("/dynamicInvoker/getAllInvokerHolders", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, dynamicInvokerResponse.getStatusCode().value());
        PaginationResponse<Map<String,Object>> body = dynamicInvokerResponse.getBody();
        Assertions.assertNotNull(body);
        List<Map<String,Object>> dynamicInvokers = body.getList();
        Assertions.assertNotEquals(0,dynamicInvokers.size());
        System.out.println("received "+dynamicInvokers.size() +" invokers: "+dynamicInvokers);


    }

    @Test
    @Order(3)
    public void testListAllDynamicInvokerMethodHolders() {
        DynamicInvokerMethodFilter request=new DynamicInvokerMethodFilter()
                .setEmptyCategories(true)
                .setCategories(Collections.singleton("TYPE_ACTION"))
                .setBasicPropertiesFilter(new BasicPropertiesFilter().setNameLike("listTests"));
        ParameterizedTypeReference<PaginationResponse<Map<String,Object>>> t= new ParameterizedTypeReference<>() {};

        ResponseEntity<PaginationResponse<Map<String,Object>>> dynamicInvokerResponse = this.restTemplate.exchange("/dynamicInvokerMethod/getAllInvokerMethodHolders", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, dynamicInvokerResponse.getStatusCode().value());
        PaginationResponse<Map<String,Object>> body = dynamicInvokerResponse.getBody();
        Assertions.assertNotNull(body);
        List<Map<String,Object>> dynamicInvokers = body.getList();
        Assertions.assertNotEquals(0,dynamicInvokers.size());
        System.out.println("received "+dynamicInvokers.size() +" invoker methods: "+dynamicInvokers);


    }



    @Test
    @Order(7)
    public void getAllInvokers() {
        ParameterizedTypeReference<PaginationResponse<Map<String,Object>>> ref= new ParameterizedTypeReference<>() {
        };
        ResponseEntity<PaginationResponse<Map<String,Object>>> clazzResponse = this.restTemplate.exchange("/dynamicInvoker/getAll", HttpMethod.POST, new HttpEntity<>(new DynamicInvokerFilter()), ref);
        Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
        PaginationResponse<Map<String,Object>> clazz = clazzResponse.getBody();
        Assertions.assertNotNull(clazz);
        logger.info("received: "+clazz.getList());

    }

    @Test
    @Order(8)
    public void createDynamicExecution() {
        String json= """
				{
					"methodName": "listTests",
					"serviceCanonicalNames": [
						"plugins.TestInvoker"
					],
					"executionParametersHolder": {
					"type":"plugins.TestFilter",
						"pageSize": 10,
						"currentPage": 0
					}
				}
				""";
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<DynamicExecution> clazzResponse = this.restTemplate.exchange("/dynamicExecution/create",HttpMethod.POST,new HttpEntity<>(json,headers), DynamicExecution.class);
        Assertions.assertEquals(200, clazzResponse.getStatusCode().value());
        dynamicExecution = clazzResponse.getBody();
        Assertions.assertNotNull(dynamicExecution);
        logger.info("received: "+dynamicExecution);

    }

    @Test
    @Order(9)
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
    @Order(10)
    public void getExample() {
        DynamicExecutionExampleRequest executeInvokerRequest=new DynamicExecutionExampleRequest()
                .setId(dynamicExecution.getId());
        ResponseEntity<Object> clazzResponse = this.restTemplate.postForEntity("/dynamicExecution/getDynamicExecutionReturnExample", executeInvokerRequest, Object.class);
        Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
        Object example = clazzResponse.getBody();
        Assertions.assertNotNull(example);
        logger.info("received: "+example);


    }

    @Test
    @Order(11)
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
    @Order(12)
    public void invokeListTestsDirect() throws JsonProcessingException {
        ParameterizedTypeReference<PaginationResponse<Map<String,Object>>> ref= new ParameterizedTypeReference<>() {
        };
        ResponseEntity<PaginationResponse<Map<String,Object>>> clazzResponse = this.restTemplate.exchange("/test/listTests", HttpMethod.POST, new HttpEntity<>(dynamicExecution.getExecutionParametersHolder()), ref);
        Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
        PaginationResponse<Map<String,Object>> clazz = clazzResponse.getBody();
        Assertions.assertNotNull(clazz);
        logger.info("received: "+clazz.getList());

    }



}
