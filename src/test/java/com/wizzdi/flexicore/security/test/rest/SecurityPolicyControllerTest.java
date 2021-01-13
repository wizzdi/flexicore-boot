package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.security.SecurityPolicy;
import com.wizzdi.flexicore.security.request.SecurityPolicyCreate;
import com.wizzdi.flexicore.security.request.SecurityPolicyFilter;
import com.wizzdi.flexicore.security.request.SecurityPolicyUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.test.app.App;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")

public class SecurityPolicyControllerTest {

    private SecurityPolicy securityPolicy;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    private void init() {
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("authenticationKey", "fake");
                    return execution.execute(request, body);
                }));

    }

    @Test
    @Order(1)
    public void testSecurityPolicyCreate() {
        String name = UUID.randomUUID().toString();
        SecurityPolicyCreate request = new SecurityPolicyCreate()
                .setName(name);
        ResponseEntity<SecurityPolicy> securityPolicyResponse = this.restTemplate.postForEntity("/securityPolicy/create", request, SecurityPolicy.class);
        Assertions.assertEquals(200, securityPolicyResponse.getStatusCodeValue());
        securityPolicy = securityPolicyResponse.getBody();
        assertSecurityPolicy(request, securityPolicy);

    }

    @Test
    @Order(2)
    public void testListAllSecurityPolicys() {
        SecurityPolicyFilter request=new SecurityPolicyFilter();
        ParameterizedTypeReference<PaginationResponse<SecurityPolicy>> t=new ParameterizedTypeReference<PaginationResponse<SecurityPolicy>>() {};

        ResponseEntity<PaginationResponse<SecurityPolicy>> securityPolicyResponse = this.restTemplate.exchange("/securityPolicy/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, securityPolicyResponse.getStatusCodeValue());
        PaginationResponse<SecurityPolicy> body = securityPolicyResponse.getBody();
        Assertions.assertNotNull(body);
        List<SecurityPolicy> securityPolicys = body.getList();
        Assertions.assertNotEquals(0,securityPolicys.size());
        Assertions.assertTrue(securityPolicys.stream().anyMatch(f->f.getId().equals(securityPolicy.getId())));


    }

    public void assertSecurityPolicy(SecurityPolicyCreate request, SecurityPolicy securityPolicy) {
        Assertions.assertNotNull(securityPolicy);
        Assertions.assertEquals(request.getName(), securityPolicy.getName());
    }

    @Test
    @Order(3)
    public void testSecurityPolicyUpdate(){
        String name = UUID.randomUUID().toString();
        SecurityPolicyUpdate request = new SecurityPolicyUpdate()
                .setId(securityPolicy.getId())
                .setName(name);
        ResponseEntity<SecurityPolicy> securityPolicyResponse = this.restTemplate.exchange("/securityPolicy/update",HttpMethod.PUT, new HttpEntity<>(request), SecurityPolicy.class);
        Assertions.assertEquals(200, securityPolicyResponse.getStatusCodeValue());
        securityPolicy = securityPolicyResponse.getBody();
        assertSecurityPolicy(request, securityPolicy);

    }

}
