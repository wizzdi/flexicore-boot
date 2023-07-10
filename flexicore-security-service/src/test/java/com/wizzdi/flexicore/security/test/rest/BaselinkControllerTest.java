package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.Baselink;
import com.wizzdi.flexicore.security.request.BaselinkCreate;
import com.wizzdi.flexicore.security.request.BaselinkFilter;
import com.wizzdi.flexicore.security.request.BaselinkUpdate;
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

public class BaselinkControllerTest {
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

    private Baselink baselink;
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
    public void testBaselinkCreate() {
        String name = UUID.randomUUID().toString();
        BaselinkCreate request = new BaselinkCreate()
                .setName(name);
        ResponseEntity<Baselink> baselinkResponse = this.restTemplate.postForEntity("/baselink/create", request, Baselink.class);
        Assertions.assertEquals(200, baselinkResponse.getStatusCodeValue());
        baselink = baselinkResponse.getBody();
        assertBaselink(request, baselink);

    }

    @Test
    @Order(2)
    public void testListAllBaselinks() {
        BaselinkFilter request=new BaselinkFilter();
        ParameterizedTypeReference<PaginationResponse<Baselink>> t=new ParameterizedTypeReference<PaginationResponse<Baselink>>() {};

        ResponseEntity<PaginationResponse<Baselink>> baselinkResponse = this.restTemplate.exchange("/baselink/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, baselinkResponse.getStatusCodeValue());
        PaginationResponse<Baselink> body = baselinkResponse.getBody();
        Assertions.assertNotNull(body);
        List<Baselink> baselinks = body.getList();
        Assertions.assertNotEquals(0,baselinks.size());
        Assertions.assertTrue(baselinks.stream().anyMatch(f->f.getId().equals(baselink.getId())));


    }

    public void assertBaselink(BaselinkCreate request, Baselink baselink) {
        Assertions.assertNotNull(baselink);
        Assertions.assertEquals(request.getName(), baselink.getName());
    }

    @Test
    @Order(3)
    public void testBaselinkUpdate(){
        String name = UUID.randomUUID().toString();
        BaselinkUpdate request = new BaselinkUpdate()
                .setId(baselink.getId())
                .setName(name);
        ResponseEntity<Baselink> baselinkResponse = this.restTemplate.exchange("/baselink/update",HttpMethod.PUT, new HttpEntity<>(request), Baselink.class);
        Assertions.assertEquals(200, baselinkResponse.getStatusCodeValue());
        baselink = baselinkResponse.getBody();
        assertBaselink(request, baselink);

    }

}
