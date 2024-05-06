package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.Role;
import com.flexicore.model.RoleToBaseclass;
import com.flexicore.model.SecurityOperation;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.RoleToBaseclassCreate;
import com.wizzdi.flexicore.security.request.RoleToBaseclassFilter;
import com.wizzdi.flexicore.security.request.RoleToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.test.app.App;
import org.apache.commons.io.IOUtils;
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
import java.nio.charset.StandardCharsets;
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


public class RoleToBaseclassControllerTest {
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

    private RoleToBaseclass roleToBaseclass;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    @Qualifier("allOps")
    @Lazy
    private SecurityOperation allOps;
    @Autowired
    @Qualifier("adminSecurityContext")
    @Lazy
    private SecurityContextBase adminSecurityContext;
    @Autowired
    @Qualifier("roleTest")
    private Role roleTest;

    @BeforeAll
    public void init() {

        ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return !response.getStatusCode().is2xxSuccessful();
            }
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                String responseBody = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
                throw new HttpClientErrorException(response.getStatusCode(), "%s:%s".formatted(response.getStatusText(), responseBody));
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
    public void testRoleToBaseclassCreate() {
        String name = UUID.randomUUID().toString();
        RoleToBaseclassCreate request = new RoleToBaseclassCreate()
                .setRoleId(roleTest.getId())
                .setAccess(IOperation.Access.allow)
                .setBaseclassId(adminSecurityContext.getUser().getSecurity().getId())
                .setOperationId(allOps.getId())
                .setName(name);
        ResponseEntity<RoleToBaseclass> roleToBaseclassResponse = this.restTemplate.postForEntity("/roleToBaseclass/create", request, RoleToBaseclass.class);
        Assertions.assertEquals(200, roleToBaseclassResponse.getStatusCode().value());
        roleToBaseclass = roleToBaseclassResponse.getBody();
        assertRoleToBaseclass(request, roleToBaseclass);

    }

    @Test
    @Order(2)
    public void testListAllRoleToBaseclasss() {
        RoleToBaseclassFilter request=new RoleToBaseclassFilter();
        ParameterizedTypeReference<PaginationResponse<RoleToBaseclass>> t= new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PaginationResponse<RoleToBaseclass>> roleToBaseclassResponse = this.restTemplate.exchange("/roleToBaseclass/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, roleToBaseclassResponse.getStatusCode().value());
        PaginationResponse<RoleToBaseclass> body = roleToBaseclassResponse.getBody();
        Assertions.assertNotNull(body);
        List<RoleToBaseclass> roleToBaseclasss = body.getList();
        Assertions.assertNotEquals(0,roleToBaseclasss.size());
        Assertions.assertTrue(roleToBaseclasss.stream().anyMatch(f->f.getId().equals(roleToBaseclass.getId())));


    }

    public void assertRoleToBaseclass(RoleToBaseclassCreate request, RoleToBaseclass roleToBaseclass) {
        Assertions.assertNotNull(roleToBaseclass);
        Assertions.assertEquals(request.getName(), roleToBaseclass.getName());
    }

    @Test
    @Order(3)
    public void testRoleToBaseclassUpdate(){
        String name = UUID.randomUUID().toString();
        RoleToBaseclassUpdate request = new RoleToBaseclassUpdate()
                .setId(roleToBaseclass.getId())
                .setName(name);
        ResponseEntity<RoleToBaseclass> roleToBaseclassResponse = this.restTemplate.exchange("/roleToBaseclass/update",HttpMethod.PUT, new HttpEntity<>(request), RoleToBaseclass.class);
        Assertions.assertEquals(200, roleToBaseclassResponse.getStatusCode().value());
        roleToBaseclass = roleToBaseclassResponse.getBody();
        assertRoleToBaseclass(request, roleToBaseclass);

    }

}
