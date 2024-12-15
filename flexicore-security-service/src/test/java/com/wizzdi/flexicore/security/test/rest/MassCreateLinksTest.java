package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.wizzdi.flexicore.security.test.app.TestEntity;
import com.wizzdi.flexicore.security.test.app.TestEntityCreate;
import com.wizzdi.flexicore.security.test.app.TestEntityService;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassMassCreate;
import com.wizzdi.flexicore.security.service.PermissionGroupToBaseclassService;
import com.wizzdi.flexicore.security.test.app.App;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour

public class MassCreateLinksTest {
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

    @Autowired
    private PermissionGroupToBaseclassService permissionGroupToBaseclassService;
    @Autowired
    private PermissionGroup permissionGroup;
    @Autowired
    private SecurityContext adminSecurityContext;
    @Autowired
    private TestEntityService testEntityService;
    private Set<String> baseclasses = new HashSet<>();

    @BeforeAll
    public void createInstances() {
        for (int i = 0; i < 10; i++) {
            TestEntity baseclass = testEntityService.createTestEntity(new TestEntityCreate().setName("as"),adminSecurityContext);
            permissionGroupToBaseclassService.merge(baseclass);
            baseclasses.add(baseclass.getId());
        }
    }

    @Test
    public void testMassCreate() {
        List<PermissionGroupToBaseclass> permissionGroupToBaseclasses = permissionGroupToBaseclassService.listAllPermissionGroupToBaseclass(new PermissionGroupToBaseclassFilter().setPermissionGroups(Collections.singletonList(permissionGroup)).setSecuredIds(baseclasses), adminSecurityContext);
        Assertions.assertTrue(permissionGroupToBaseclasses.isEmpty());
        Map<String, Map<String, PermissionGroupToBaseclass>> stringMapMap = permissionGroupToBaseclassService.massCreatePermissionLinks(new PermissionGroupToBaseclassMassCreate().setPermissionGroups(Collections.singletonList(permissionGroup)).setSecuredIds(baseclasses), adminSecurityContext);
        Map<String, PermissionGroupToBaseclass> stringPermissionGroupToBaseclassMap = stringMapMap.get(permissionGroup.getId());
        Assertions.assertNotNull(stringPermissionGroupToBaseclassMap);
        Assertions.assertEquals(baseclasses.size(), stringPermissionGroupToBaseclassMap.size());
        permissionGroupToBaseclassService.massCreatePermissionLinks(new PermissionGroupToBaseclassMassCreate().setPermissionGroups(Collections.singletonList(permissionGroup)).setSecuredIds(baseclasses), adminSecurityContext);
        permissionGroupToBaseclasses = permissionGroupToBaseclassService.listAllPermissionGroupToBaseclass(new PermissionGroupToBaseclassFilter().setPermissionGroups(Collections.singletonList(permissionGroup)).setSecuredIds(baseclasses), adminSecurityContext);
        Assertions.assertEquals(baseclasses.size(), permissionGroupToBaseclasses.size());
    }


}
