package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.SecurityLink;
import com.wizzdi.flexicore.security.request.SecurityLinkCreate;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkUpdate;
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


public class SecurityLinkControllerTest {
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

    private SecurityLink securityLink;
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
    public void testSecurityLinkCreate() {
        String name = UUID.randomUUID().toString();
        SecurityLinkCreate request = new SecurityLinkCreate()
                .setSimpleValue("allow")
                .setName(name);
        ResponseEntity<SecurityLink> securityLinkResponse = this.restTemplate.postForEntity("/securityLink/create", request, SecurityLink.class);
        Assertions.assertEquals(200, securityLinkResponse.getStatusCodeValue());
        securityLink = securityLinkResponse.getBody();
        assertSecurityLink(request, securityLink);

    }

    @Test
    @Order(2)
    public void testListAllSecurityLinks() {
        SecurityLinkFilter request=new SecurityLinkFilter();
        ParameterizedTypeReference<PaginationResponse<SecurityLink>> t=new ParameterizedTypeReference<PaginationResponse<SecurityLink>>() {};

        ResponseEntity<PaginationResponse<SecurityLink>> securityLinkResponse = this.restTemplate.exchange("/securityLink/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, securityLinkResponse.getStatusCodeValue());
        PaginationResponse<SecurityLink> body = securityLinkResponse.getBody();
        Assertions.assertNotNull(body);
        List<SecurityLink> securityLinks = body.getList();
        Assertions.assertNotEquals(0,securityLinks.size());
        Assertions.assertTrue(securityLinks.stream().anyMatch(f->f.getId().equals(securityLink.getId())));


    }

    public void assertSecurityLink(SecurityLinkCreate request, SecurityLink securityLink) {
        Assertions.assertNotNull(securityLink);
        Assertions.assertEquals(request.getName(), securityLink.getName());
    }

    @Test
    @Order(3)
    public void testSecurityLinkUpdate(){
        String name = UUID.randomUUID().toString();
        SecurityLinkUpdate request = new SecurityLinkUpdate()
                .setId(securityLink.getId())
                .setName(name);
        ResponseEntity<SecurityLink> securityLinkResponse = this.restTemplate.exchange("/securityLink/update",HttpMethod.PUT, new HttpEntity<>(request), SecurityLink.class);
        Assertions.assertEquals(200, securityLinkResponse.getStatusCodeValue());
        securityLink = securityLinkResponse.getBody();
        assertSecurityLink(request, securityLink);

    }

}
