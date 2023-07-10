package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.TenantToBaseClassPremission;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionCreate;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionFilter;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionUpdate;
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


public class TenantToBaseclassControllerTest {
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

    private TenantToBaseClassPremission tenantToBaseClassPremission;
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
    public void testTenantToBaseClassPremissionCreate() {
        String name = UUID.randomUUID().toString();
        TenantToBaseclassPermissionCreate request = new TenantToBaseclassPermissionCreate()
                .setName(name);
        ResponseEntity<TenantToBaseClassPremission> tenantToBaseClassPremissionResponse = this.restTemplate.postForEntity("/tenantToBaseclassPermission/create", request, TenantToBaseClassPremission.class);
        Assertions.assertEquals(200, tenantToBaseClassPremissionResponse.getStatusCodeValue());
        tenantToBaseClassPremission = tenantToBaseClassPremissionResponse.getBody();
        assertTenantToBaseClassPremission(request, tenantToBaseClassPremission);

    }

    @Test
    @Order(2)
    public void testListAllTenantToBaseClassPremissions() {
        TenantToBaseclassPermissionFilter request=new TenantToBaseclassPermissionFilter();
        ParameterizedTypeReference<PaginationResponse<TenantToBaseClassPremission>> t=new ParameterizedTypeReference<PaginationResponse<TenantToBaseClassPremission>>() {};

        ResponseEntity<PaginationResponse<TenantToBaseClassPremission>> tenantToBaseClassPremissionResponse = this.restTemplate.exchange("/tenantToBaseclassPermission/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, tenantToBaseClassPremissionResponse.getStatusCodeValue());
        PaginationResponse<TenantToBaseClassPremission> body = tenantToBaseClassPremissionResponse.getBody();
        Assertions.assertNotNull(body);
        List<TenantToBaseClassPremission> tenantToBaseClassPremissions = body.getList();
        Assertions.assertNotEquals(0,tenantToBaseClassPremissions.size());
        Assertions.assertTrue(tenantToBaseClassPremissions.stream().anyMatch(f->f.getId().equals(tenantToBaseClassPremission.getId())));


    }

    public void assertTenantToBaseClassPremission(TenantToBaseclassPermissionCreate request, TenantToBaseClassPremission tenantToBaseClassPremission) {
        Assertions.assertNotNull(tenantToBaseClassPremission);
        Assertions.assertEquals(request.getName(), tenantToBaseClassPremission.getName());
    }

    @Test
    @Order(3)
    public void testTenantToBaseClassPremissionUpdate(){
        String name = UUID.randomUUID().toString();
        TenantToBaseclassPermissionUpdate request = new TenantToBaseclassPermissionUpdate()
                .setId(tenantToBaseClassPremission.getId())
                .setName(name);
        ResponseEntity<TenantToBaseClassPremission> tenantToBaseClassPremissionResponse = this.restTemplate.exchange("/tenantToBaseclassPermission/update",HttpMethod.PUT, new HttpEntity<>(request), TenantToBaseClassPremission.class);
        Assertions.assertEquals(200, tenantToBaseClassPremissionResponse.getStatusCodeValue());
        tenantToBaseClassPremission = tenantToBaseClassPremissionResponse.getBody();
        assertTenantToBaseClassPremission(request, tenantToBaseClassPremission);

    }

}
