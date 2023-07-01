package com.test.rest;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Role;
import com.flexicore.model.security.PasswordSecurityPolicy;
import com.flexicore.request.*;
import com.flexicore.response.AuthenticationResponse;
import com.test.init.FlexiCoreApplication;
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

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FlexiCoreApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")

public class PasswordSecurityPolicyTest {

    private PasswordSecurityPolicy passwordSecurityPolicy;
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

    @Test
    @Order(1)
    public void testPasswordSecurityPolicyCreate() {
        String name = UUID.randomUUID().toString();

        RoleCreate roleCreate = new RoleCreate()
                .setName(name);
        ResponseEntity<Role> roleResponse = this.restTemplate.postForEntity("/FlexiCore/rest/roles/createRole", roleCreate, Role.class);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
        Role role = roleResponse.getBody();
        Assertions.assertNotNull(role);

        PasswordSecurityPolicyCreate request = new PasswordSecurityPolicyCreate()
                .setForceCapital(false)
                .setForceDigits(false)
                .setForceLetters(false)
                .setForceLowerCase(true)
                .setMinLength(20)
                .setPolicyRoleId(role.getId())
                .setPolicyTenantId(role.getTenant().getId())
                .setEnabled(true)
                .setStartTime(OffsetDateTime.now())
                .setName(name);
        ResponseEntity<PasswordSecurityPolicy> passwordSecurityPolicyResponse = this.restTemplate.postForEntity("/passwordSecurityPolicy/create", request, PasswordSecurityPolicy.class);
        Assertions.assertEquals(200, passwordSecurityPolicyResponse.getStatusCodeValue());
        passwordSecurityPolicy = passwordSecurityPolicyResponse.getBody();
        assertPasswordSecurityPolicy(request, passwordSecurityPolicy);

    }

    @Test
    @Order(2)
    public void testListAllPasswordSecurityPolicys() {
        PasswordSecurityPolicyFilter request=new PasswordSecurityPolicyFilter()
                .setRolesIds(Collections.singleton(passwordSecurityPolicy.getPolicyRole().getId()))
                .setSecurityTenantsIds(Collections.singleton(passwordSecurityPolicy.getPolicyTenant().getId()));
        ParameterizedTypeReference<PaginationResponse<PasswordSecurityPolicy>> t=new ParameterizedTypeReference<PaginationResponse<PasswordSecurityPolicy>>() {};

        ResponseEntity<PaginationResponse<PasswordSecurityPolicy>> passwordSecurityPolicyResponse = this.restTemplate.exchange("/passwordSecurityPolicy/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, passwordSecurityPolicyResponse.getStatusCodeValue());
        PaginationResponse<PasswordSecurityPolicy> body = passwordSecurityPolicyResponse.getBody();
        Assertions.assertNotNull(body);
        List<PasswordSecurityPolicy> passwordSecurityPolicys = body.getList();
        Assertions.assertNotEquals(0,passwordSecurityPolicys.size());
        Assertions.assertTrue(passwordSecurityPolicys.stream().anyMatch(f->f.getId().equals(passwordSecurityPolicy.getId())));


    }

    public void assertPasswordSecurityPolicy(PasswordSecurityPolicyCreate request, PasswordSecurityPolicy passwordSecurityPolicy) {
        Assertions.assertNotNull(passwordSecurityPolicy);
        Assertions.assertEquals(request.getName(), passwordSecurityPolicy.getName());
    }

    @Test
    @Order(3)
    public void testPasswordSecurityPolicyUpdate(){
        String name = UUID.randomUUID().toString();
        PasswordSecurityPolicyUpdate request = new PasswordSecurityPolicyUpdate()
                .setId(passwordSecurityPolicy.getId())
                .setName(name);
        ResponseEntity<PasswordSecurityPolicy> passwordSecurityPolicyResponse = this.restTemplate.exchange("/passwordSecurityPolicy/update",HttpMethod.PUT, new HttpEntity<>(request), PasswordSecurityPolicy.class);
        Assertions.assertEquals(200, passwordSecurityPolicyResponse.getStatusCodeValue());
        passwordSecurityPolicy = passwordSecurityPolicyResponse.getBody();
        assertPasswordSecurityPolicy(request, passwordSecurityPolicy);

    }

}
