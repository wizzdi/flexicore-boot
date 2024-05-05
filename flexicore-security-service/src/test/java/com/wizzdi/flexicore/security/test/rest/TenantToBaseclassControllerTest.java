package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.SecurityOperation;
import com.flexicore.model.TenantToBaseclass;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.TenantToBaseclassCreate;
import com.wizzdi.flexicore.security.request.TenantToBaseclassFilter;
import com.wizzdi.flexicore.security.request.TenantToBaseclassUpdate;
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

    private TenantToBaseclass tenantToBaseclass;
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
        TenantToBaseclassCreate request = new TenantToBaseclassCreate()
                .setTenantId(adminSecurityContext.getTenantToCreateIn().getId())
                .setAccess(IOperation.Access.allow)
                .setBaseclassId(adminSecurityContext.getUser().getSecurity().getId())
                .setOperationId(allOps.getId())
                .setName(name);
        ResponseEntity<TenantToBaseclass> tenantToBaseClassPremissionResponse = this.restTemplate.postForEntity("/tenantToBaseclass/create", request, TenantToBaseclass.class);
        Assertions.assertEquals(200, tenantToBaseClassPremissionResponse.getStatusCode().value());
        tenantToBaseclass = tenantToBaseClassPremissionResponse.getBody();
        assertTenantToBaseClassPremission(request, tenantToBaseclass);

    }

    @Test
    @Order(2)
    public void testListAllTenantToBaseClassPremissions() {
        TenantToBaseclassFilter request=new TenantToBaseclassFilter();
        ParameterizedTypeReference<PaginationResponse<TenantToBaseclass>> t=new ParameterizedTypeReference<PaginationResponse<TenantToBaseclass>>() {};

        ResponseEntity<PaginationResponse<TenantToBaseclass>> tenantToBaseClassPremissionResponse = this.restTemplate.exchange("/tenantToBaseclass/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, tenantToBaseClassPremissionResponse.getStatusCode().value());
        PaginationResponse<TenantToBaseclass> body = tenantToBaseClassPremissionResponse.getBody();
        Assertions.assertNotNull(body);
        List<TenantToBaseclass> tenantToBaseclasses = body.getList();
        Assertions.assertNotEquals(0, tenantToBaseclasses.size());
        Assertions.assertTrue(tenantToBaseclasses.stream().anyMatch(f->f.getId().equals(tenantToBaseclass.getId())));


    }

    public void assertTenantToBaseClassPremission(TenantToBaseclassCreate request, TenantToBaseclass tenantToBaseclass) {
        Assertions.assertNotNull(tenantToBaseclass);
        Assertions.assertEquals(request.getName(), tenantToBaseclass.getName());
    }

    @Test
    @Order(3)
    public void testTenantToBaseClassPremissionUpdate(){
        String name = UUID.randomUUID().toString();
        TenantToBaseclassUpdate request = new TenantToBaseclassUpdate()
                .setId(tenantToBaseclass.getId())
                .setName(name);
        ResponseEntity<TenantToBaseclass> tenantToBaseClassPremissionResponse = this.restTemplate.exchange("/tenantToBaseclass/update",HttpMethod.PUT, new HttpEntity<>(request), TenantToBaseclass.class);
        Assertions.assertEquals(200, tenantToBaseClassPremissionResponse.getStatusCode().value());
        tenantToBaseclass = tenantToBaseClassPremissionResponse.getBody();
        assertTenantToBaseClassPremission(request, tenantToBaseclass);

    }

}
