package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.UserToBaseClass;
import com.wizzdi.flexicore.security.request.UserToBaseclassCreate;
import com.wizzdi.flexicore.security.request.UserToBaseclassFilter;
import com.wizzdi.flexicore.security.request.UserToBaseclassUpdate;
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


public class UserToBaseclassControllerTest {
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

    private UserToBaseClass userToBaseClass;
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
    public void testUserToBaseClassCreate() {
        String name = UUID.randomUUID().toString();
        UserToBaseclassCreate request = new UserToBaseclassCreate()
                .setName(name);
        ResponseEntity<UserToBaseClass> userToBaseClassResponse = this.restTemplate.postForEntity("/userToBaseclass/create", request, UserToBaseClass.class);
        Assertions.assertEquals(200, userToBaseClassResponse.getStatusCodeValue());
        userToBaseClass = userToBaseClassResponse.getBody();
        assertUserToBaseClass(request, userToBaseClass);

    }

    @Test
    @Order(2)
    public void testListAllUserToBaseClasss() {
        UserToBaseclassFilter request=new UserToBaseclassFilter();
        ParameterizedTypeReference<PaginationResponse<UserToBaseClass>> t=new ParameterizedTypeReference<PaginationResponse<UserToBaseClass>>() {};

        ResponseEntity<PaginationResponse<UserToBaseClass>> userToBaseClassResponse = this.restTemplate.exchange("/userToBaseclass/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, userToBaseClassResponse.getStatusCodeValue());
        PaginationResponse<UserToBaseClass> body = userToBaseClassResponse.getBody();
        Assertions.assertNotNull(body);
        List<UserToBaseClass> userToBaseClasss = body.getList();
        Assertions.assertNotEquals(0,userToBaseClasss.size());
        Assertions.assertTrue(userToBaseClasss.stream().anyMatch(f->f.getId().equals(userToBaseClass.getId())));


    }

    public void assertUserToBaseClass(UserToBaseclassCreate request, UserToBaseClass userToBaseClass) {
        Assertions.assertNotNull(userToBaseClass);
        Assertions.assertEquals(request.getName(), userToBaseClass.getName());
    }

    @Test
    @Order(3)
    public void testUserToBaseClassUpdate(){
        String name = UUID.randomUUID().toString();
        UserToBaseclassUpdate request = new UserToBaseclassUpdate()
                .setId(userToBaseClass.getId())
                .setName(name);
        ResponseEntity<UserToBaseClass> userToBaseClassResponse = this.restTemplate.exchange("/userToBaseclass/update",HttpMethod.PUT, new HttpEntity<>(request), UserToBaseClass.class);
        Assertions.assertEquals(200, userToBaseClassResponse.getStatusCodeValue());
        userToBaseClass = userToBaseClassResponse.getBody();
        assertUserToBaseClass(request, userToBaseClass);

    }

}
