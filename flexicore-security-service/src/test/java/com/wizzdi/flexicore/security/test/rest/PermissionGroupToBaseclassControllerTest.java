package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.PermissionGroup;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.test.app.App;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
import org.testcontainers.junit.jupiter.Container;
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


public class PermissionGroupToBaseclassControllerTest {
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

    private PermissionGroupToBaseclass permissionGroupToBaseclass;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PermissionGroup permissionGroup;
    @Autowired
    @Lazy
    @Qualifier("adminSecurityContext")
    private SecurityContextBase adminSecurityContext;

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
    public void testPermissionGroupToBaseclassCreate() {
        String name = UUID.randomUUID().toString();
        PermissionGroupToBaseclassCreate request = new PermissionGroupToBaseclassCreate()
                .setBaseclassId(permissionGroup.getSecurity().getId())
                .setPermissionGroupId(permissionGroup.getId())
                .setName(name);
        ResponseEntity<PermissionGroupToBaseclass> permissionGroupToBaseclassResponse = this.restTemplate.postForEntity("/permissionGroupToBaseclass/create", request, PermissionGroupToBaseclass.class);
        Assertions.assertEquals(200, permissionGroupToBaseclassResponse.getStatusCode().value());
        permissionGroupToBaseclass = permissionGroupToBaseclassResponse.getBody();
        assertPermissionGroupToBaseclass(request, permissionGroupToBaseclass);

    }

    @Test
    @Order(2)
    public void testListAllPermissionGroupToBaseclasss() {
        PermissionGroupToBaseclassFilter request=new PermissionGroupToBaseclassFilter();
        ParameterizedTypeReference<PaginationResponse<PermissionGroupToBaseclass>> t=new ParameterizedTypeReference<PaginationResponse<PermissionGroupToBaseclass>>() {};

        ResponseEntity<PaginationResponse<PermissionGroupToBaseclass>> permissionGroupToBaseclassResponse = this.restTemplate.exchange("/permissionGroupToBaseclass/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, permissionGroupToBaseclassResponse.getStatusCode().value());
        PaginationResponse<PermissionGroupToBaseclass> body = permissionGroupToBaseclassResponse.getBody();
        Assertions.assertNotNull(body);
        List<PermissionGroupToBaseclass> permissionGroupToBaseclasss = body.getList();
        Assertions.assertNotEquals(0,permissionGroupToBaseclasss.size());
        Assertions.assertTrue(permissionGroupToBaseclasss.stream().anyMatch(f->f.getId().equals(permissionGroupToBaseclass.getId())));


    }

    public void assertPermissionGroupToBaseclass(PermissionGroupToBaseclassCreate request, PermissionGroupToBaseclass permissionGroupToBaseclass) {
        Assertions.assertNotNull(permissionGroupToBaseclass);
        Assertions.assertEquals(request.getName(), permissionGroupToBaseclass.getName());
    }

    @Test
    @Order(3)
    public void testPermissionGroupToBaseclassUpdate(){
        String name = UUID.randomUUID().toString();
        PermissionGroupToBaseclassUpdate request = new PermissionGroupToBaseclassUpdate()
                .setId(permissionGroupToBaseclass.getId())
                .setName(name);
        ResponseEntity<PermissionGroupToBaseclass> permissionGroupToBaseclassResponse = this.restTemplate.exchange("/permissionGroupToBaseclass/update",HttpMethod.PUT, new HttpEntity<>(request), PermissionGroupToBaseclass.class);
        Assertions.assertEquals(200, permissionGroupToBaseclassResponse.getStatusCode().value());
        permissionGroupToBaseclass = permissionGroupToBaseclassResponse.getBody();
        assertPermissionGroupToBaseclass(request, permissionGroupToBaseclass);

    }

}
