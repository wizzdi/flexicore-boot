package com.test.rest;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Tenant;
import com.flexicore.request.*;
import com.flexicore.response.AuthenticationResponse;
import com.test.init.FlexiCoreApplication;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FlexiCoreApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class TenantRESTServiceTest {
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

    private Tenant tenant;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void init() {
        ResponseEntity<AuthenticationResponse> authenticationResponse = this.restTemplate.postForEntity("/FlexiCore/rest/authenticationNew/login", new AuthenticationRequest().setEmail("admin@flexicore.com").setPassword("admin"), AuthenticationResponse.class);
        String authenticationKey = authenticationResponse.getBody().getAuthenticationKey();
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("authenticationKey", authenticationKey);
                    return execution.execute(request, body);
                }));
    }

    @Test
    @Order(1)
    public void testTenantCreate() {
        String name = UUID.randomUUID().toString();
        UserCreate userCreate=new UserCreate()
                .setPassword(name+"_pass")
                .setEmail("admin@"+name+".com")
                .setPhoneNumber(name+"_phone")
                .setName(name+"_user");
        TenantCreate request = new TenantCreate()
                .setTenantAdmin(userCreate)
                .setName(name);
        ResponseEntity<Tenant> tenantResponse = this.restTemplate.postForEntity("/FlexiCore/rest/tenant/createTenant", request, Tenant.class);
        Assertions.assertEquals(200, tenantResponse.getStatusCodeValue());
        tenant = tenantResponse.getBody();
        assertTenant(request, tenant);

    }

    @Test
    @Order(2)
    public void testListAllTenants() {
        TenantFilter request=new TenantFilter();
        request.setNameLike(tenant.getName());
        ParameterizedTypeReference<PaginationResponse<Tenant>> t=new ParameterizedTypeReference<PaginationResponse<Tenant>>() {};

        ResponseEntity<PaginationResponse<Tenant>> tenantResponse = this.restTemplate.exchange("/FlexiCore/rest/tenant/getAllTenants", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, tenantResponse.getStatusCodeValue());
        PaginationResponse<Tenant> body = tenantResponse.getBody();
        Assertions.assertNotNull(body);
        List<Tenant> tenants = body.getList();
        Assertions.assertNotEquals(0,tenants.size());
        Assertions.assertTrue(tenants.stream().anyMatch(f->f.getId().equals(tenant.getId())));


    }

    public void assertTenant(TenantCreate request, Tenant tenant) {
        Assertions.assertNotNull(tenant);
        Assertions.assertEquals(request.getName(), tenant.getName());
        if(request.getTenantAdmin()!=null){
            ResponseEntity<AuthenticationResponse> authenticationResponse = this.restTemplate.postForEntity("/FlexiCore/rest/authenticationNew/login", new AuthenticationRequest().setEmail(request.getTenantAdmin().getEmail()).setPassword(request.getTenantAdmin().getPassword()), AuthenticationResponse.class);
            Assertions.assertEquals(200, authenticationResponse.getStatusCodeValue());
            AuthenticationResponse body = authenticationResponse.getBody();
            Assertions.assertNotNull(body);
            String userToken = body.getAuthenticationKey();
            Assertions.assertNotNull(userToken);
        }

    }

    @Test
    @Order(3)
    public void testTenantUpdate(){
        String name = UUID.randomUUID().toString();
        TenantUpdate request = new TenantUpdate()
                .setId(tenant.getId())
                .setName(name);
        ResponseEntity<Tenant> tenantResponse = this.restTemplate.postForEntity("/FlexiCore/rest/tenant/updateTenant",request, Tenant.class);
        Assertions.assertEquals(200, tenantResponse.getStatusCodeValue());
        tenant = tenantResponse.getBody();
        assertTenant(request, tenant);

    }

}
