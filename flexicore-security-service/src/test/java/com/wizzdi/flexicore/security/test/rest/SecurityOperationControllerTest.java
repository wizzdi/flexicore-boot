package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.request.SecurityOperationCreate;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.wizzdi.flexicore.security.request.SecurityOperationUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.test.app.App;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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


public class SecurityOperationControllerTest {
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

    private SecurityOperation securityOperation;
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
    public void testSecurityOperationCreate() {
        String name = UUID.randomUUID().toString();
        SecurityOperationCreate request = new SecurityOperationCreate()
                .setName(name);
        ResponseEntity<SecurityOperation> securityOperationResponse = this.restTemplate.postForEntity("/securityOperation/create", request, SecurityOperation.class);
        Assertions.assertEquals(200, securityOperationResponse.getStatusCodeValue());
        securityOperation = securityOperationResponse.getBody();
        assertSecurityOperation(request, securityOperation);

    }

    @Test
    @Order(2)
    public void testListAllSecurityOperations() {
        SecurityOperationFilter request=new SecurityOperationFilter();
        ParameterizedTypeReference<PaginationResponse<SecurityOperation>> t=new ParameterizedTypeReference<PaginationResponse<SecurityOperation>>() {};

        ResponseEntity<PaginationResponse<SecurityOperation>> securityOperationResponse = this.restTemplate.exchange("/securityOperation/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, securityOperationResponse.getStatusCodeValue());
        PaginationResponse<SecurityOperation> body = securityOperationResponse.getBody();
        Assertions.assertNotNull(body);
        List<SecurityOperation> securityOperations = body.getList();
        Assertions.assertNotEquals(0,securityOperations.size());
        Assertions.assertTrue(securityOperations.stream().anyMatch(f->f.getId().equals(securityOperation.getId())));


    }

    public void assertSecurityOperation(SecurityOperationCreate request, SecurityOperation securityOperation) {
        Assertions.assertNotNull(securityOperation);
        Assertions.assertEquals(request.getName(), securityOperation.getName());
    }

    @Test
    @Order(3)
    public void testSecurityOperationUpdate(){
        String name = UUID.randomUUID().toString();
        SecurityOperationUpdate request = new SecurityOperationUpdate()
                .setId(securityOperation.getId())
                .setName(name);
        ResponseEntity<SecurityOperation> securityOperationResponse = this.restTemplate.exchange("/securityOperation/update",HttpMethod.PUT, new HttpEntity<>(request), SecurityOperation.class);
        Assertions.assertEquals(200, securityOperationResponse.getStatusCodeValue());
        securityOperation = securityOperationResponse.getBody();
        assertSecurityOperation(request, securityOperation);

    }

}
