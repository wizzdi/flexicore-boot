package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.security.request.PermissionGroupCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupUpdate;
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

public class PermissionGroupControllerTest {

    private PermissionGroup permissionGroup;
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
    public void testPermissionGroupCreate() {
        String name = UUID.randomUUID().toString();
        PermissionGroupCreate request = new PermissionGroupCreate()
                .setName(name);
        ResponseEntity<PermissionGroup> permissionGroupResponse = this.restTemplate.postForEntity("/permissionGroup/create", request, PermissionGroup.class);
        Assertions.assertEquals(200, permissionGroupResponse.getStatusCodeValue());
        permissionGroup = permissionGroupResponse.getBody();
        assertPermissionGroup(request, permissionGroup);

    }

    @Test
    @Order(2)
    public void testListAllPermissionGroups() {
        PermissionGroupFilter request=new PermissionGroupFilter();
        ParameterizedTypeReference<PaginationResponse<PermissionGroup>> t=new ParameterizedTypeReference<PaginationResponse<PermissionGroup>>() {};

        ResponseEntity<PaginationResponse<PermissionGroup>> permissionGroupResponse = this.restTemplate.exchange("/permissionGroup/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, permissionGroupResponse.getStatusCodeValue());
        PaginationResponse<PermissionGroup> body = permissionGroupResponse.getBody();
        Assertions.assertNotNull(body);
        List<PermissionGroup> permissionGroups = body.getList();
        Assertions.assertNotEquals(0,permissionGroups.size());
        Assertions.assertTrue(permissionGroups.stream().anyMatch(f->f.getId().equals(permissionGroup.getId())));


    }

    public void assertPermissionGroup(PermissionGroupCreate request, PermissionGroup permissionGroup) {
        Assertions.assertNotNull(permissionGroup);
        Assertions.assertEquals(request.getName(), permissionGroup.getName());
    }

    @Test
    @Order(3)
    public void testPermissionGroupUpdate(){
        String name = UUID.randomUUID().toString();
        PermissionGroupUpdate request = new PermissionGroupUpdate()
                .setId(permissionGroup.getId())
                .setName(name);
        ResponseEntity<PermissionGroup> permissionGroupResponse = this.restTemplate.exchange("/permissionGroup/update",HttpMethod.PUT, new HttpEntity<>(request), PermissionGroup.class);
        Assertions.assertEquals(200, permissionGroupResponse.getStatusCodeValue());
        permissionGroup = permissionGroupResponse.getBody();
        assertPermissionGroup(request, permissionGroup);

    }

}
