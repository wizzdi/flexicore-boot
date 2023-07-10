package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import com.wizzdi.flexicore.security.request.SecurityUserFilter;
import com.wizzdi.flexicore.security.request.SecurityUserUpdate;
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


public class SecurityUserControllerTest {
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

    private SecurityUser securityUser;
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
    public void testSecurityUserCreate() {
        String name = UUID.randomUUID().toString();
        SecurityUserCreate request = new SecurityUserCreate()
                .setName(name);
        ResponseEntity<SecurityUser> securityUserResponse = this.restTemplate.postForEntity("/securityUser/create", request, SecurityUser.class);
        Assertions.assertEquals(200, securityUserResponse.getStatusCodeValue());
        securityUser = securityUserResponse.getBody();
        assertSecurityUser(request, securityUser);

    }

    @Test
    @Order(2)
    public void testListAllSecurityUsers() {
        SecurityUserFilter request=new SecurityUserFilter();
        ParameterizedTypeReference<PaginationResponse<SecurityUser>> t=new ParameterizedTypeReference<PaginationResponse<SecurityUser>>() {};

        ResponseEntity<PaginationResponse<SecurityUser>> securityUserResponse = this.restTemplate.exchange("/securityUser/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, securityUserResponse.getStatusCodeValue());
        PaginationResponse<SecurityUser> body = securityUserResponse.getBody();
        Assertions.assertNotNull(body);
        List<SecurityUser> securityUsers = body.getList();
        Assertions.assertNotEquals(0,securityUsers.size());
        Assertions.assertTrue(securityUsers.stream().anyMatch(f->f.getId().equals(securityUser.getId())));


    }

    public void assertSecurityUser(SecurityUserCreate request, SecurityUser securityUser) {
        Assertions.assertNotNull(securityUser);
        Assertions.assertEquals(request.getName(), securityUser.getName());
    }

    @Test
    @Order(3)
    public void testSecurityUserUpdate(){
        String name = UUID.randomUUID().toString();
        SecurityUserUpdate request = new SecurityUserUpdate()
                .setId(securityUser.getId())
                .setName(name);
        ResponseEntity<SecurityUser> securityUserResponse = this.restTemplate.exchange("/securityUser/update",HttpMethod.PUT, new HttpEntity<>(request), SecurityUser.class);
        Assertions.assertEquals(200, securityUserResponse.getStatusCodeValue());
        securityUser = securityUserResponse.getBody();
        assertSecurityUser(request, securityUser);

    }

}
