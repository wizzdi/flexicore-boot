package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.TenantToUser;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;
import com.wizzdi.flexicore.security.request.TenantToUserFilter;
import com.wizzdi.flexicore.security.request.TenantToUserUpdate;
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


public class TenantToUserControllerTest {
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

    private TenantToUser tenantToUser;
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
    public void testTenantToUserCreate() {
        String name = UUID.randomUUID().toString();
        TenantToUserCreate request = new TenantToUserCreate()
                .setName(name);
        ResponseEntity<TenantToUser> tenantToUserResponse = this.restTemplate.postForEntity("/tenantToUser/create", request, TenantToUser.class);
        Assertions.assertEquals(200, tenantToUserResponse.getStatusCode().value());
        tenantToUser = tenantToUserResponse.getBody();
        assertTenantToUser(request, tenantToUser);

    }

    @Test
    @Order(2)
    public void testListAllTenantToUsers() {
        TenantToUserFilter request=new TenantToUserFilter();
        ParameterizedTypeReference<PaginationResponse<TenantToUser>> t=new ParameterizedTypeReference<PaginationResponse<TenantToUser>>() {};

        ResponseEntity<PaginationResponse<TenantToUser>> tenantToUserResponse = this.restTemplate.exchange("/tenantToUser/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, tenantToUserResponse.getStatusCode().value());
        PaginationResponse<TenantToUser> body = tenantToUserResponse.getBody();
        Assertions.assertNotNull(body);
        List<TenantToUser> tenantToUsers = body.getList();
        Assertions.assertNotEquals(0,tenantToUsers.size());
        Assertions.assertTrue(tenantToUsers.stream().anyMatch(f->f.getId().equals(tenantToUser.getId())));


    }

    public void assertTenantToUser(TenantToUserCreate request, TenantToUser tenantToUser) {
        Assertions.assertNotNull(tenantToUser);
        Assertions.assertEquals(request.getName(), tenantToUser.getName());
    }

    @Test
    @Order(3)
    public void testTenantToUserUpdate(){
        String name = UUID.randomUUID().toString();
        TenantToUserUpdate request = new TenantToUserUpdate()
                .setId(tenantToUser.getId())
                .setName(name);
        ResponseEntity<TenantToUser> tenantToUserResponse = this.restTemplate.exchange("/tenantToUser/update",HttpMethod.PUT, new HttpEntity<>(request), TenantToUser.class);
        Assertions.assertEquals(200, tenantToUserResponse.getStatusCode().value());
        tenantToUser = tenantToUserResponse.getBody();
        assertTenantToUser(request, tenantToUser);

    }

}
