package com.wizzdi.security.bearer.jwt;

import com.flexicore.model.Role;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.security.bearer.jwt.request.AuthRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")

public class TestControllerTest {

    private Test test;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void init() {
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("/api/public/login", new AuthRequest().setPassword("admin").setUsername("admin"), String.class);
        Assertions.assertEquals(200,stringResponseEntity.getStatusCodeValue());
        String body1 = stringResponseEntity.getBody();
        List<String> strings = stringResponseEntity.getHeaders().get(HttpHeaders.AUTHORIZATION);
        Assertions.assertNotNull(strings);
        Assertions.assertFalse(strings.isEmpty());
        String key=strings.get(0);
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .setBearerAuth(key);
                    return execution.execute(request, body);
                }));

    }


    @Test
    @Order(2)
    public void testListAllTests() {
        ParameterizedTypeReference<PaginationResponse<Role>> t=new ParameterizedTypeReference<PaginationResponse<Role>>() {};

        ResponseEntity<PaginationResponse<Role>> testResponse = this.restTemplate.exchange("/test/getAll", HttpMethod.GET, new HttpEntity<>(null,null), t);
        Assertions.assertEquals(200, testResponse.getStatusCodeValue());
        PaginationResponse<Role> body = testResponse.getBody();
        Assertions.assertNotNull(body);
        List<Role> tests = body.getList();



    }


}