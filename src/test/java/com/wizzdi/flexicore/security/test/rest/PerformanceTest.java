package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.model.Role;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.RoleService;
import com.wizzdi.flexicore.security.test.app.App;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

public class PerformanceTest {

   @Autowired
   private RoleService roleService;
    @Autowired
    @Qualifier("adminSecurityContext")
    private SecurityContextBase<?,?,?,?> securityContextBase;
    private String id=null;

    private static final Logger logger= LoggerFactory.getLogger(PerformanceTest.class);

    @Test
    @Order(1)
    public void testCreateRoles(){
        RoleCreate roleCreate=new RoleCreate();
        long start=System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {

            roleCreate.setName("role "+i);
            Role role = roleService.createRole(roleCreate, securityContextBase);
            if(i % 10000==0){
                logger.info("create roles ("+i+")"+": "+(System.currentTimeMillis()-start) +"ms , passed");
            }
            if(i==500000){
                id=role.getId();
            }
        }


    }

    @Test
    @Order(2)
    public void testGetRole(){
        long start=System.currentTimeMillis();
        Role role = roleService.getByIdOrNull(id, Role.class, null);
        logger.info("fetch role: "+(System.currentTimeMillis()-start) +"ms , passed");



    }

}
