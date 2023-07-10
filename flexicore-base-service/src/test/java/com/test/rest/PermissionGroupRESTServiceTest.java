package com.test.rest;

import com.flexicore.data.jsoncontainers.CreatePermissionGroupRequest;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.PermissionGroup;
import com.flexicore.request.AuthenticationRequest;
import com.flexicore.request.PermissionGroupsFilter;
import com.flexicore.request.UpdatePermissionGroup;
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


public class PermissionGroupRESTServiceTest {
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

    private PermissionGroup permissionGroup;
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
    public void testCreatePermissionGroupRequest() {
        String name = UUID.randomUUID().toString();
        CreatePermissionGroupRequest request = new CreatePermissionGroupRequest()
                .setName(name);
        ResponseEntity<PermissionGroup> permissionGroupResponse = this.restTemplate.postForEntity("/FlexiCore/rest/permissionGroup/createPermissionGroup", request, PermissionGroup.class);
        Assertions.assertEquals(200, permissionGroupResponse.getStatusCodeValue());
        permissionGroup = permissionGroupResponse.getBody();
        assertPermissionGroup(request, permissionGroup);

    }

    @Test
    @Order(2)
    public void testListAllPermissionGroups() {
        PermissionGroupsFilter request=new PermissionGroupsFilter();
        request.setNameLike(permissionGroup.getName());
        ParameterizedTypeReference<PaginationResponse<PermissionGroup>> t=new ParameterizedTypeReference<PaginationResponse<PermissionGroup>>() {};

        ResponseEntity<PaginationResponse<PermissionGroup>> permissionGroupResponse = this.restTemplate.exchange("/FlexiCore/rest/permissionGroup/getAllPermissionGroups", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, permissionGroupResponse.getStatusCodeValue());
        PaginationResponse<PermissionGroup> body = permissionGroupResponse.getBody();
        Assertions.assertNotNull(body);
        List<PermissionGroup> permissionGroups = body.getList();
        Assertions.assertNotEquals(0,permissionGroups.size());
        Assertions.assertTrue(permissionGroups.stream().anyMatch(f->f.getId().equals(permissionGroup.getId())));


    }

    public void assertPermissionGroup(CreatePermissionGroupRequest request, PermissionGroup permissionGroup) {
        Assertions.assertNotNull(permissionGroup);
        Assertions.assertEquals(request.getName(), permissionGroup.getName());
    }

    @Test
    @Order(3)
    public void testUpdatePermissionGroup(){
        String name = UUID.randomUUID().toString();
        UpdatePermissionGroup request = new UpdatePermissionGroup()
                .setId(permissionGroup.getId());
        request
                .setName(name);
        ResponseEntity<PermissionGroup> permissionGroupResponse = this.restTemplate.postForEntity("/FlexiCore/rest/permissionGroup/updatePermissionGroup", request, PermissionGroup.class);
        Assertions.assertEquals(200, permissionGroupResponse.getStatusCodeValue());
        permissionGroup = permissionGroupResponse.getBody();
        assertPermissionGroup(request, permissionGroup);

    }

}
