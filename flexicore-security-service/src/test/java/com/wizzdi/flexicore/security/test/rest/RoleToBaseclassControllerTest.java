package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.RoleToBaseclass;
import com.wizzdi.flexicore.security.request.RoleToBaseclassCreate;
import com.wizzdi.flexicore.security.request.RoleToBaseclassFilter;
import com.wizzdi.flexicore.security.request.RoleToBaseclassUpdate;
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

public class RoleToBaseclassControllerTest {

    private RoleToBaseclass roleToBaseclass;
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
    public void testRoleToBaseclassCreate() {
        String name = UUID.randomUUID().toString();
        RoleToBaseclassCreate request = new RoleToBaseclassCreate()
                .setName(name);
        ResponseEntity<RoleToBaseclass> roleToBaseclassResponse = this.restTemplate.postForEntity("/roleToBaseclass/create", request, RoleToBaseclass.class);
        Assertions.assertEquals(200, roleToBaseclassResponse.getStatusCodeValue());
        roleToBaseclass = roleToBaseclassResponse.getBody();
        assertRoleToBaseclass(request, roleToBaseclass);

    }

    @Test
    @Order(2)
    public void testListAllRoleToBaseclasss() {
        RoleToBaseclassFilter request=new RoleToBaseclassFilter();
        ParameterizedTypeReference<PaginationResponse<RoleToBaseclass>> t=new ParameterizedTypeReference<PaginationResponse<RoleToBaseclass>>() {};

        ResponseEntity<PaginationResponse<RoleToBaseclass>> roleToBaseclassResponse = this.restTemplate.exchange("/roleToBaseclass/getAll", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, roleToBaseclassResponse.getStatusCodeValue());
        PaginationResponse<RoleToBaseclass> body = roleToBaseclassResponse.getBody();
        Assertions.assertNotNull(body);
        List<RoleToBaseclass> roleToBaseclasss = body.getList();
        Assertions.assertNotEquals(0,roleToBaseclasss.size());
        Assertions.assertTrue(roleToBaseclasss.stream().anyMatch(f->f.getId().equals(roleToBaseclass.getId())));


    }

    public void assertRoleToBaseclass(RoleToBaseclassCreate request, RoleToBaseclass roleToBaseclass) {
        Assertions.assertNotNull(roleToBaseclass);
        Assertions.assertEquals(request.getName(), roleToBaseclass.getName());
    }

    @Test
    @Order(3)
    public void testRoleToBaseclassUpdate(){
        String name = UUID.randomUUID().toString();
        RoleToBaseclassUpdate request = new RoleToBaseclassUpdate()
                .setId(roleToBaseclass.getId())
                .setName(name);
        ResponseEntity<RoleToBaseclass> roleToBaseclassResponse = this.restTemplate.exchange("/roleToBaseclass/update",HttpMethod.PUT, new HttpEntity<>(request), RoleToBaseclass.class);
        Assertions.assertEquals(200, roleToBaseclassResponse.getStatusCodeValue());
        roleToBaseclass = roleToBaseclassResponse.getBody();
        assertRoleToBaseclass(request, roleToBaseclass);

    }

}
