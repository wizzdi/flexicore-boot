package com.test.rest;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Role;
import com.flexicore.request.AuthenticationRequest;
import com.flexicore.response.AuthenticationResponse;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.impl.DynamicInvokersService;
import com.test.init.FlexiCoreApplication;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FlexiCoreApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class DynamicInvokersRESTServiceTest {
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
    @Autowired
    private DynamicInvokersService dynamicInvokersService;

    @Autowired
    @Lazy
    private SecurityContext adminSecurityContext;

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
    @Order(2)
    public void testGetAllInvokers() {
        DynamicInvokerFilter request = new DynamicInvokerFilter()
                .setPageSize(10).setCurrentPage(0);
        ParameterizedTypeReference<PaginationResponse<InvokerInfo>> t = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PaginationResponse<InvokerInfo>> roleResponse = this.restTemplate.exchange("/FlexiCore/rest/dynamicInvokers/getAllInvokers", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
        PaginationResponse<InvokerInfo> body = roleResponse.getBody();
        Assertions.assertNotNull(body);
        List<InvokerInfo> roles = body.getList();
        Assertions.assertNotEquals(0, roles.size());


    }


}
