package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.RoleToUser;
import com.wizzdi.flexicore.security.request.RoleToUserCreate;
import com.wizzdi.flexicore.security.request.RoleToUserFilter;
import com.wizzdi.flexicore.security.request.RoleToUserUpdate;
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

public class RoleToUserControllerTest {

    private RoleToUser roleToUser;
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
    public void testRoleToUserCreate() {
        String name = UUID.randomUUID().toString();
        RoleToUserCreate request = new RoleToUserCreate()
                .setName(name);
        ResponseEntity<RoleToUser> roleToUserResponse = this.restTemplate.postForEntity("/roleToUser/create", request, RoleToUser.class);
        Assertions.assertEquals(200, roleToUserResponse.getStatusCodeValue());
        roleToUser = roleToUserResponse.getBody();
        assertRoleToUser(request, roleToUser);

    }

    @Test
    @Order(2)
    public void testListAllRoleToUsers() {
        RoleToUserFilter request=new RoleToUserFilter();
        ParameterizedTypeReference<PaginationResponse<RoleToUser>> t=new ParameterizedTypeReference<PaginationResponse<RoleToUser>>() {};

        ResponseEntity<PaginationResponse<RoleToUser>> roleToUserResponse = this.restTemplate.exchange("/roleToUser/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, roleToUserResponse.getStatusCodeValue());
        PaginationResponse<RoleToUser> body = roleToUserResponse.getBody();
        Assertions.assertNotNull(body);
        List<RoleToUser> roleToUsers = body.getList();
        Assertions.assertNotEquals(0,roleToUsers.size());
        Assertions.assertTrue(roleToUsers.stream().anyMatch(f->f.getId().equals(roleToUser.getId())));


    }

    public void assertRoleToUser(RoleToUserCreate request, RoleToUser roleToUser) {
        Assertions.assertNotNull(roleToUser);
        Assertions.assertEquals(request.getName(), roleToUser.getName());
    }

    @Test
    @Order(3)
    public void testRoleToUserUpdate(){
        String name = UUID.randomUUID().toString();
        RoleToUserUpdate request = new RoleToUserUpdate()
                .setId(roleToUser.getId())
                .setName(name);
        ResponseEntity<RoleToUser> roleToUserResponse = this.restTemplate.exchange("/roleToUser/update",HttpMethod.PUT, new HttpEntity<>(request), RoleToUser.class);
        Assertions.assertEquals(200, roleToUserResponse.getStatusCodeValue());
        roleToUser = roleToUserResponse.getBody();
        assertRoleToUser(request, roleToUser);

    }

}
