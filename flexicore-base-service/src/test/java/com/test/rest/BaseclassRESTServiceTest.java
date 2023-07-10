package com.test.rest;

import com.flexicore.model.Role;
import com.flexicore.request.AuthenticationRequest;
import com.flexicore.request.MassDeleteRequest;
import com.flexicore.request.RoleCreate;
import com.flexicore.response.AuthenticationResponse;
import com.flexicore.response.MassDeleteResponse;
import com.test.init.FlexiCoreApplication;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FlexiCoreApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class BaseclassRESTServiceTest {
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

    private Role createRole() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("categoryName",System.currentTimeMillis()+"");
        HttpEntity<?> request=new HttpEntity<>(new RoleCreate().setName("test"),headers);
        ResponseEntity<Role> createCategory = this.restTemplate.exchange("/FlexiCore/rest/roles/createRole",HttpMethod.POST, request, Role.class);
        return createCategory.getBody();



    }

    @Test
    @Order(1)
    public void testMassDelete() {
        String name = UUID.randomUUID().toString();
        Role role = createRole();
        MassDeleteRequest request = new MassDeleteRequest()
                .setIds(Collections.singleton(role.getId()));
        ResponseEntity<MassDeleteResponse> roleResponse = this.restTemplate.postForEntity("/FlexiCore/rest/baseclass/massDelete", request, MassDeleteResponse.class);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
        MassDeleteResponse body = roleResponse.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertEquals(body.getDeletedIds(),request.getIds());
        ResponseEntity<String> categoryResponse = this.restTemplate.getForEntity("/FlexiCore/rest/baseclass/getbyid/"+role.getId()+"/"+Role.class.getCanonicalName(), String.class);
        Assertions.assertEquals(400,categoryResponse.getStatusCodeValue());

        roleResponse = this.restTemplate.postForEntity("/FlexiCore/rest/baseclass/massDelete", request, MassDeleteResponse.class);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
        body = roleResponse.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.getDeletedIds().isEmpty());


    }


}
