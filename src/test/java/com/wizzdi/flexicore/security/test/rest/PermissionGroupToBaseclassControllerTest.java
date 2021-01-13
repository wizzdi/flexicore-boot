package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.PermissionGroupToBaseclass;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassUpdate;
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

public class PermissionGroupToBaseclassControllerTest {

    private PermissionGroupToBaseclass permissionGroupToBaseclass;
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
    public void testPermissionGroupToBaseclassCreate() {
        String name = UUID.randomUUID().toString();
        PermissionGroupToBaseclassCreate request = new PermissionGroupToBaseclassCreate()
                .setName(name);
        ResponseEntity<PermissionGroupToBaseclass> permissionGroupToBaseclassResponse = this.restTemplate.postForEntity("/permissionGroupToBaseclass/create", request, PermissionGroupToBaseclass.class);
        Assertions.assertEquals(200, permissionGroupToBaseclassResponse.getStatusCodeValue());
        permissionGroupToBaseclass = permissionGroupToBaseclassResponse.getBody();
        assertPermissionGroupToBaseclass(request, permissionGroupToBaseclass);

    }

    @Test
    @Order(2)
    public void testListAllPermissionGroupToBaseclasss() {
        PermissionGroupToBaseclassFilter request=new PermissionGroupToBaseclassFilter();
        ParameterizedTypeReference<PaginationResponse<PermissionGroupToBaseclass>> t=new ParameterizedTypeReference<PaginationResponse<PermissionGroupToBaseclass>>() {};

        ResponseEntity<PaginationResponse<PermissionGroupToBaseclass>> permissionGroupToBaseclassResponse = this.restTemplate.exchange("/permissionGroupToBaseclass/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, permissionGroupToBaseclassResponse.getStatusCodeValue());
        PaginationResponse<PermissionGroupToBaseclass> body = permissionGroupToBaseclassResponse.getBody();
        Assertions.assertNotNull(body);
        List<PermissionGroupToBaseclass> permissionGroupToBaseclasss = body.getList();
        Assertions.assertNotEquals(0,permissionGroupToBaseclasss.size());
        Assertions.assertTrue(permissionGroupToBaseclasss.stream().anyMatch(f->f.getId().equals(permissionGroupToBaseclass.getId())));


    }

    public void assertPermissionGroupToBaseclass(PermissionGroupToBaseclassCreate request, PermissionGroupToBaseclass permissionGroupToBaseclass) {
        Assertions.assertNotNull(permissionGroupToBaseclass);
        Assertions.assertEquals(request.getName(), permissionGroupToBaseclass.getName());
    }

    @Test
    @Order(3)
    public void testPermissionGroupToBaseclassUpdate(){
        String name = UUID.randomUUID().toString();
        PermissionGroupToBaseclassUpdate request = new PermissionGroupToBaseclassUpdate()
                .setId(permissionGroupToBaseclass.getId())
                .setName(name);
        ResponseEntity<PermissionGroupToBaseclass> permissionGroupToBaseclassResponse = this.restTemplate.exchange("/permissionGroupToBaseclass/update",HttpMethod.PUT, new HttpEntity<>(request), PermissionGroupToBaseclass.class);
        Assertions.assertEquals(200, permissionGroupToBaseclassResponse.getStatusCodeValue());
        permissionGroupToBaseclass = permissionGroupToBaseclassResponse.getBody();
        assertPermissionGroupToBaseclass(request, permissionGroupToBaseclass);

    }

}
