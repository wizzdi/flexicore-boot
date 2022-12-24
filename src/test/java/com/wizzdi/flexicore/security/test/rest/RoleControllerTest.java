package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.Role;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.request.RoleFilter;
import com.wizzdi.flexicore.security.request.RoleUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.test.app.App;
import com.wizzdi.flexicore.security.validation.ValidationErrorResponse;
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

public class RoleControllerTest {

    private Role role;
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
    public void testRoleCreate() {
        String name = UUID.randomUUID().toString();
        RoleCreate request = new RoleCreate()
                .setName(name);
        ResponseEntity<Role> roleResponse = this.restTemplate.postForEntity("/role/create", request, Role.class);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
        role = roleResponse.getBody();
        assertRole(request, role);

    }

    @Test
    @Order(2)
    public void testListAllRoles() {
        RoleFilter request=new RoleFilter();
        ParameterizedTypeReference<PaginationResponse<Role>> t=new ParameterizedTypeReference<PaginationResponse<Role>>() {};

        ResponseEntity<PaginationResponse<Role>> roleResponse = this.restTemplate.exchange("/role/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
        PaginationResponse<Role> body = roleResponse.getBody();
        Assertions.assertNotNull(body);
        List<Role> roles = body.getList();
        Assertions.assertNotEquals(0,roles.size());
        Assertions.assertTrue(roles.stream().anyMatch(f->f.getId().equals(role.getId())));


    }

    public void assertRole(RoleCreate request, Role role) {
        Assertions.assertNotNull(role);
        Assertions.assertEquals(request.getName(), role.getName());
    }

    @Test
    @Order(3)
    public void testRoleUpdate(){
        String name = UUID.randomUUID().toString();
        RoleUpdate request = new RoleUpdate()
                .setId(role.getId())
                .setName(name);
        ResponseEntity<Role> roleResponse = this.restTemplate.exchange("/role/update",HttpMethod.PUT, new HttpEntity<>(request), Role.class);
        Assertions.assertEquals(200, roleResponse.getStatusCodeValue());
        role = roleResponse.getBody();
        assertRole(request, role);

    }

    @Test
    @Order(4)
    public void testRoleUpdateValidationNoId(){
        String name = UUID.randomUUID().toString();
        RoleUpdate request = new RoleUpdate()
                .setName(name);
        ResponseEntity<ValidationErrorResponse> roleResponse = this.restTemplate.exchange("/role/update",HttpMethod.PUT, new HttpEntity<>(request), ValidationErrorResponse.class);
        Assertions.assertEquals(400, roleResponse.getStatusCodeValue());
        ValidationErrorResponse body = roleResponse.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.getViolations().isEmpty());
        Assertions.assertTrue(body.getViolations().stream().allMatch(f->f.getFieldName().equals("id")));
        System.out.println(body);


    }

    @Test
    @Order(5)
    public void testRoleUpdateValidationBadId(){
        String name = UUID.randomUUID().toString();
        RoleUpdate request = new RoleUpdate()
                .setId("test")
                .setName(name);
        ResponseEntity<ValidationErrorResponse> roleResponse = this.restTemplate.exchange("/role/update",HttpMethod.PUT, new HttpEntity<>(request), ValidationErrorResponse.class);
        Assertions.assertEquals(400, roleResponse.getStatusCodeValue());
        ValidationErrorResponse body = roleResponse.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.getViolations().isEmpty());
        Assertions.assertTrue(body.getViolations().stream().allMatch(f->f.getFieldName().equals("id")));
        System.out.println(body);


    }

}
