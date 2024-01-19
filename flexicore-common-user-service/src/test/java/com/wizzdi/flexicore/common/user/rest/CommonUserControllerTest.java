package com.wizzdi.flexicore.common.user.rest;

import com.flexicore.model.User;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.common.user.App;
import com.wizzdi.flexicore.common.user.request.CommonUserCreate;
import com.wizzdi.flexicore.common.user.request.CommonUserFilter;
import com.wizzdi.flexicore.common.user.request.CommonUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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

public class CommonUserControllerTest {
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

    private User user;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
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
    public void testCommonUserCreate() {
        String name = UUID.randomUUID().toString();
        CommonUserCreate request = new CommonUserCreate()
                .setPassword("test")
                .setEmail(name + "@test.com")
                .setTenantId(adminSecurityContext.getTenantToCreateIn().getId())
                .setName(name);
        ResponseEntity<User> UserResponse = this.restTemplate.postForEntity("/commonUser/createUser", request, User.class);
        Assertions.assertEquals(200, UserResponse.getStatusCodeValue());
        user = UserResponse.getBody();
        assertUser(request, user);

    }

    @Test
    @Order(2)
    public void testListAllUsers() {
        CommonUserFilter request=new CommonUserFilter();
        ParameterizedTypeReference<PaginationResponse<User>> t=new ParameterizedTypeReference<PaginationResponse<User>>() {};

        ResponseEntity<PaginationResponse<User>> UserResponse = this.restTemplate.exchange("/commonUser/getAllUsers", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, UserResponse.getStatusCodeValue());
        PaginationResponse<User> body = UserResponse.getBody();
        Assertions.assertNotNull(body);
        List<User> Users = body.getList();
        Assertions.assertNotEquals(0,Users.size());
        Assertions.assertTrue(Users.stream().anyMatch(f->f.getId().equals(user.getId())));


    }

    public void assertUser(CommonUserCreate request, User User) {
        Assertions.assertNotNull(User);
        Assertions.assertEquals(request.getName(), User.getName());
    }

    @Test
    @Order(3)
    public void testCommonUserUpdate(){
        String name = UUID.randomUUID().toString();
        CommonUserUpdate request = new CommonUserUpdate()
                .setId(user.getId())
                .setName(name);
        ResponseEntity<User> UserResponse = this.restTemplate.exchange("/commonUser/updateUser",HttpMethod.PUT, new HttpEntity<>(request), User.class);
        Assertions.assertEquals(200, UserResponse.getStatusCodeValue());
        user = UserResponse.getBody();
        assertUser(request, user);

    }

}
