package com.test.rest;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Role;
import com.flexicore.request.AuthenticationRequest;
import com.flexicore.request.RoleCreate;
import com.flexicore.request.RoleFilter;
import com.flexicore.request.RoleUpdate;
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


public class RoleRESTServiceTest {
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
    public void testRoleCreate() {
        String name = UUID.randomUUID().toString();
        RoleCreate request = new RoleCreate()
                .setName(name);
        ResponseEntity<Role> roleResponse = this.restTemplate.postForEntity("/FlexiCore/rest/roles/createRole", request, Role.class);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
        role = roleResponse.getBody();
        assertRole(request, role);

    }

    @Test
    @Order(2)
    public void testListAllRoles() {
        RoleFilter request=new RoleFilter()
                .setNames(Collections.singleton(role.getName()));
        ParameterizedTypeReference<PaginationResponse<Role>> t=new ParameterizedTypeReference<PaginationResponse<Role>>() {};

        ResponseEntity<PaginationResponse<Role>> roleResponse = this.restTemplate.exchange("/FlexiCore/rest/roles/getAllRoles", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
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
        ResponseEntity<Role> roleResponse = this.restTemplate.exchange("/FlexiCore/rest/roles/updateRole",HttpMethod.PUT, new HttpEntity<>(request), Role.class);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
        role = roleResponse.getBody();
        assertRole(request, role);

    }

}
