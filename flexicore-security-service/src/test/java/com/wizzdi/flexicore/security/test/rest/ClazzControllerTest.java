package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.Clazz;
import com.wizzdi.flexicore.security.request.ClazzCreate;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import com.wizzdi.flexicore.security.request.ClazzUpdate;
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

public class ClazzControllerTest {
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

    private Clazz clazz;
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
    public void testClazzCreate() {
        String name = UUID.randomUUID().toString();
        ClazzCreate request = new ClazzCreate()
                .setName(name);
        ResponseEntity<Clazz> clazzResponse = this.restTemplate.postForEntity("/clazz/create", request, Clazz.class);
        Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
        clazz = clazzResponse.getBody();
        assertClazz(request, clazz);

    }

    @Test
    @Order(2)
    public void testListAllClazzs() {
        ClazzFilter request=new ClazzFilter();
        ParameterizedTypeReference<PaginationResponse<Clazz>> t=new ParameterizedTypeReference<PaginationResponse<Clazz>>() {};

        ResponseEntity<PaginationResponse<Clazz>> clazzResponse = this.restTemplate.exchange("/clazz/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
        PaginationResponse<Clazz> body = clazzResponse.getBody();
        Assertions.assertNotNull(body);
        List<Clazz> clazzs = body.getList();
        Assertions.assertNotEquals(0,clazzs.size());
        Assertions.assertTrue(clazzs.stream().anyMatch(f->f.getId().equals(clazz.getId())));


    }

    public void assertClazz(ClazzCreate request, Clazz clazz) {
        Assertions.assertNotNull(clazz);
        Assertions.assertEquals(request.getName(), clazz.getName());
    }

    @Test
    @Order(3)
    public void testClazzUpdate(){
        String name = UUID.randomUUID().toString();
        ClazzUpdate request = new ClazzUpdate()
                .setId(clazz.getId())
                .setName(name);
        ResponseEntity<Clazz> clazzResponse = this.restTemplate.exchange("/clazz/update",HttpMethod.PUT, new HttpEntity<>(request), Clazz.class);
        Assertions.assertEquals(200, clazzResponse.getStatusCodeValue());
        clazz = clazzResponse.getBody();
        assertClazz(request, clazz);

    }

}
