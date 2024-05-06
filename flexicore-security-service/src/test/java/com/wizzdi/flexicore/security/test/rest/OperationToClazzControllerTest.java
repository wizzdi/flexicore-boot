package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.OperationToClazz;
import com.wizzdi.flexicore.security.request.OperationToClazzCreate;
import com.wizzdi.flexicore.security.request.OperationToClazzFilter;
import com.wizzdi.flexicore.security.request.OperationToClazzUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.test.app.App;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour

public class OperationToClazzControllerTest {
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

    private OperationToClazz operationToClazz;
    @Autowired
    private TestRestTemplate restTemplate;

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
    public void testOperationToClazzCreate() {
        String name = UUID.randomUUID().toString();
        OperationToClazzCreate request = new OperationToClazzCreate()
                .setName(name);
        ResponseEntity<OperationToClazz> operationToClazzResponse = this.restTemplate.postForEntity("/operationToClazz/create", request, OperationToClazz.class);
        Assertions.assertEquals(200, operationToClazzResponse.getStatusCode().value());
        operationToClazz = operationToClazzResponse.getBody();
        assertOperationToClazz(request, operationToClazz);

    }

    @Test
    @Order(2)
    public void testListAllOperationToClazzs() {
        OperationToClazzFilter request=new OperationToClazzFilter();
        ParameterizedTypeReference<PaginationResponse<OperationToClazz>> t= new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PaginationResponse<OperationToClazz>> operationToClazzResponse = this.restTemplate.exchange("/operationToClazz/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, operationToClazzResponse.getStatusCode().value());
        PaginationResponse<OperationToClazz> body = operationToClazzResponse.getBody();
        Assertions.assertNotNull(body);
        List<OperationToClazz> operationToClazzs = body.getList();
        Assertions.assertNotEquals(0,operationToClazzs.size());
        Assertions.assertTrue(operationToClazzs.stream().anyMatch(f->f.getId().equals(operationToClazz.getId())));


    }

    public void assertOperationToClazz(OperationToClazzCreate request, OperationToClazz operationToClazz) {
        Assertions.assertNotNull(operationToClazz);
        Assertions.assertEquals(request.getName(), operationToClazz.getName());
    }

    @Test
    @Order(3)
    public void testOperationToClazzUpdate(){
        String name = UUID.randomUUID().toString();
        OperationToClazzUpdate request = new OperationToClazzUpdate()
                .setId(operationToClazz.getId())
                .setName(name);
        ResponseEntity<OperationToClazz> operationToClazzResponse = this.restTemplate.exchange("/operationToClazz/update",HttpMethod.PUT, new HttpEntity<>(request), OperationToClazz.class);
        Assertions.assertEquals(200, operationToClazzResponse.getStatusCode().value());
        operationToClazz = operationToClazzResponse.getBody();
        assertOperationToClazz(request, operationToClazz);

    }

}
