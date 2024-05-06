package com.wizzdi.flexicore.security.test.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flexicore.model.Role;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.request.RoleFilter;
import com.wizzdi.flexicore.security.request.RoleUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.test.app.App;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
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


public class RoleControllerTest {
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

    private Role role;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    @Lazy
    @Qualifier("adminSecurityContext")
    private SecurityContextBase adminSecurityContext;

    private static final ObjectMapper objectMapper=new ObjectMapper().registerModule(new JavaTimeModule()).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    @BeforeAll
    public void init() {
        ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return !response.getStatusCode().is2xxSuccessful();
            }
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                throw new HttpClientErrorException(response.getStatusCode(),response.getStatusText());
            }
        };
        restTemplate.getRestTemplate().setErrorHandler(errorHandler);
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("authenticationKey", "fake");
                    return execution.execute(request, body);
                }));


    }


    @Test
    @Order(1)
    public void testRoleCreate() {
        String name = UUID.randomUUID().toString();
        RoleCreate request = new RoleCreate()
                .setTenantId(adminSecurityContext.getTenantToCreateIn().getId())
                .setName(name);
        ResponseEntity<Role> roleResponse = this.restTemplate.postForEntity("/role/create", request, Role.class);
        Assertions.assertEquals(200, roleResponse.getStatusCode().value());
        role = roleResponse.getBody();
        assertRole(request, role);

    }

    @Test
    @Order(2)
    public void testListAllRoles() {
        RoleFilter request=new RoleFilter();
        ParameterizedTypeReference<PaginationResponse<Role>> t= new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PaginationResponse<Role>> roleResponse = this.restTemplate.exchange("/role/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, roleResponse.getStatusCode().value());
        PaginationResponse<Role> body = roleResponse.getBody();
        Assertions.assertNotNull(body);
        List<Role> roles = body.getList();
        Assertions.assertNotEquals(0,roles.size());
        Assertions.assertTrue(roles.stream().anyMatch(f->f.getId().equals(role.getId())));


    }

    public void assertRole(RoleCreate request, Role role) {
        Assertions.assertNotNull(role);
        Assertions.assertEquals(request.getName(), role.getName());
    }

    @Test
    @Order(3)
    public void testRoleUpdate(){
        String name = UUID.randomUUID().toString();
        RoleUpdate request = new RoleUpdate()
                .setId(role.getId())
                .setName(name);
        ResponseEntity<Role> roleResponse = this.restTemplate.exchange("/role/update",HttpMethod.PUT, new HttpEntity<>(request), Role.class);
        Assertions.assertEquals(200, roleResponse.getStatusCode().value());
        role = roleResponse.getBody();
        assertRole(request, role);

    }


}
