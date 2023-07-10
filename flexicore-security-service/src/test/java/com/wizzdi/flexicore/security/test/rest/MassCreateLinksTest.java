package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassMassCreate;
import com.wizzdi.flexicore.security.service.PermissionGroupToBaseclassService;
import com.wizzdi.flexicore.security.test.app.App;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    private SecurityContextBase adminSecurityContext;
    private List<Baseclass> baseclasses = new ArrayList<>();

    @BeforeAll
    public void createInstances() {
        for (int i = 0; i < 10; i++) {
            Baseclass baseclass = new Baseclass("as", adminSecurityContext);
            permissionGroupToBaseclassService.merge(baseclass);
            baseclasses.add(baseclass);
        }
    }

    @Test
    public void testMassCreate() {
        List<PermissionGroupToBaseclass> permissionGroupToBaseclasses = permissionGroupToBaseclassService.listAllPermissionGroupToBaseclass(new PermissionGroupToBaseclassFilter().setLeftside(Collections.singletonList(permissionGroup)).setRightside(baseclasses), adminSecurityContext);
        Assertions.assertTrue(permissionGroupToBaseclasses.isEmpty());
        Map<String, Map<String, PermissionGroupToBaseclass>> stringMapMap = permissionGroupToBaseclassService.massCreatePermissionLinks(new PermissionGroupToBaseclassMassCreate().setPermissionGroups(Collections.singletonList(permissionGroup)).setBaseclasses(baseclasses), adminSecurityContext);
        Map<String, PermissionGroupToBaseclass> stringPermissionGroupToBaseclassMap = stringMapMap.get(permissionGroup.getId());
        Assertions.assertNotNull(stringPermissionGroupToBaseclassMap);
        Assertions.assertEquals(baseclasses.size(), stringPermissionGroupToBaseclassMap.size());
        permissionGroupToBaseclassService.massCreatePermissionLinks(new PermissionGroupToBaseclassMassCreate().setPermissionGroups(Collections.singletonList(permissionGroup)).setBaseclasses(baseclasses), adminSecurityContext);
        permissionGroupToBaseclasses = permissionGroupToBaseclassService.listAllPermissionGroupToBaseclass(new PermissionGroupToBaseclassFilter().setLeftside(Collections.singletonList(permissionGroup)).setRightside(baseclasses), adminSecurityContext);
        Assertions.assertEquals(baseclasses.size(), permissionGroupToBaseclasses.size());
    }


}
