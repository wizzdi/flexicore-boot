package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;
import com.wizzdi.flexicore.security.request.SecurityTenantFilter;
import com.wizzdi.flexicore.security.request.SecurityTenantUpdate;
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


public class SecurityTenantControllerTest {

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

    private SecurityTenant securityTenant;
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
    public void testSecurityTenantCreate() {
        String name = UUID.randomUUID().toString();
        SecurityTenantCreate request = new SecurityTenantCreate()
                .setName(name);
        ResponseEntity<SecurityTenant> securityTenantResponse = this.restTemplate.postForEntity("/securityTenant/create", request, SecurityTenant.class);
        Assertions.assertEquals(200, securityTenantResponse.getStatusCodeValue());
        securityTenant = securityTenantResponse.getBody();
        assertSecurityTenant(request, securityTenant);

    }

    @Test
    @Order(2)
    public void testListAllSecurityTenants() {
        SecurityTenantFilter request=new SecurityTenantFilter();
        ParameterizedTypeReference<PaginationResponse<SecurityTenant>> t=new ParameterizedTypeReference<PaginationResponse<SecurityTenant>>() {};

        ResponseEntity<PaginationResponse<SecurityTenant>> securityTenantResponse = this.restTemplate.exchange("/securityTenant/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, securityTenantResponse.getStatusCodeValue());
        PaginationResponse<SecurityTenant> body = securityTenantResponse.getBody();
        Assertions.assertNotNull(body);
        List<SecurityTenant> securityTenants = body.getList();
        Assertions.assertNotEquals(0,securityTenants.size());
        Assertions.assertTrue(securityTenants.stream().anyMatch(f->f.getId().equals(securityTenant.getId())));


    }

    public void assertSecurityTenant(SecurityTenantCreate request, SecurityTenant securityTenant) {
        Assertions.assertNotNull(securityTenant);
        Assertions.assertEquals(request.getName(), securityTenant.getName());
    }

    @Test
    @Order(3)
    public void testSecurityTenantUpdate(){
        String name = UUID.randomUUID().toString();
        SecurityTenantUpdate request = new SecurityTenantUpdate()
                .setId(securityTenant.getId())
                .setName(name);
        ResponseEntity<SecurityTenant> securityTenantResponse = this.restTemplate.exchange("/securityTenant/update",HttpMethod.PUT, new HttpEntity<>(request), SecurityTenant.class);
        Assertions.assertEquals(200, securityTenantResponse.getStatusCodeValue());
        securityTenant = securityTenantResponse.getBody();
        assertSecurityTenant(request, securityTenant);

    }

}
