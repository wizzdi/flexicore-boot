package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.rest.All;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.interfaces.SecurityContextProvider;
import com.wizzdi.flexicore.security.request.*;
import com.wizzdi.flexicore.security.service.*;
import com.wizzdi.flexicore.security.test.app.*;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class SecurityQueryTest {
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
private SecurityUserService securityUserService;
    @Autowired
    private TenantToUserService tenantToUserService;
    @Autowired
    private SecurityTenantService securityTenantService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleToUserService roleToUserService;

    @Autowired
    private SecurityContextProvider securityContextProvider;
    @Autowired
    private TestEntityService testEntityService;



    @Autowired
    private SecurityContextBase adminSecurityContext;
    @Autowired
    private UserToBaseclassService userToBaseclassService;
    @Autowired
    private PermissionGroupToBaseclassService permissionGroupToBaseclassService;
    @Autowired
    private RoleToBaseclassService roleToBaseclassService;
    @Autowired
    private TenantToBaseclassPermissionService tenantToBaseclassPermissionService;
    @Autowired
    private PermissionGroupService permissionGroupService;

    private SecurityContextBase testUserSecurityContext;
    private Role testRole;
    private  SecurityTenant testTenant;
    private SecurityUser testUser;

    private Set<String> othersInTenantIds=new HashSet<>();
    private Set<String> othersInOtherTenantIds =new HashSet<>();
    @Autowired
    private SecurityOperationService securityOperationService;

    private SecurityOperation allOperation;


    @BeforeAll
    public void init() {
        testUser= securityUserService.createSecurityUser(new SecurityUserCreate().setName("testUser"), null);
        testTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("testTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setSecurityUser(testUser).setDefaultTenant(true).setTenant(testTenant),null);
        testRole= roleService.createRole(new RoleCreate().setTenant(testTenant).setName("testRole"), null);
        testRole.setTenant(testTenant);
        testRole.setCreator(testUser);
        roleService.merge(testRole);
        roleToUserService.createRoleToUser(new RoleToUserCreate().setRole(testRole).setSecurityUser(testUser),null);
        testUserSecurityContext=securityContextProvider.getSecurityContext(testUser);
        othersInOtherTenantIds =IntStream.range(0, 10).mapToObj(f->testEntityService.createTestEntity(new TestEntityCreate().setName("othersInOtherTenant"+f),adminSecurityContext)).map(f->f.getId()).collect(Collectors.toSet());
        List<TestEntity> list = IntStream.range(0, 10).mapToObj(f -> testEntityService.createTestEntity(new TestEntityCreate().setName("othersInTenant" + f), adminSecurityContext)).toList();
        for (TestEntity testEntity : list) {
            testEntity.getSecurity().setTenant(testTenant);
            testEntityService.merge(testEntity);
        }
        othersInTenantIds= list.stream().map(f->f.getId()).collect(Collectors.toSet());
        allOperation=securityOperationService.getByIdOrNull(Baseclass.generateUUIDFromString(All.class.getCanonicalName()),SecurityOperation.class,adminSecurityContext);
        Assertions.assertNotNull(allOperation);

    }

    @Test
    @Order(1)
    public void testForCreator() {
        TestEntity forCreator = testEntityService.createTestEntity(new TestEntityCreate().setName("forCreator"), testUserSecurityContext);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forCreator.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(2)
    public void testForUser() {
        TestEntity forUser = testEntityService.createTestEntityNoMerge(new TestEntityCreate().setName("forUser"), adminSecurityContext);
        forUser.getSecurity().setTenant(testTenant);
        testEntityService.merge(forUser);
        UserToBaseClass userToBaseclass = userToBaseclassService.createUserToBaseclass(new UserToBaseclassCreate().setUser(testUser).setBaseclass(forUser.getSecurity()).setValue(allOperation).setSimpleValue(IOperation.Access.allow.name()), null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forUser.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(3)
    public void testForRole() {
        TestEntity forRole = testEntityService.createTestEntity(new TestEntityCreate().setName("forRole"), adminSecurityContext);
        forRole.getSecurity().setTenant(testTenant);
        testEntityService.merge(forRole);
        roleToBaseclassService.createRoleToBaseclass(new RoleToBaseclassCreate().setRole(testRole).setBaseclass(forRole.getSecurity()).setValue(allOperation).setSimpleValue(IOperation.Access.allow.name()),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forRole.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(4)
    public void testForTenant() {
        TestEntity forTenant = testEntityService.createTestEntity(new TestEntityCreate().setName("forTenant"), adminSecurityContext);

        tenantToBaseclassPermissionService.createTenantToBaseclassPermission(new TenantToBaseclassPermissionCreate().setBaseclass(forTenant.getSecurity()).setValue(allOperation).setSimpleValue(IOperation.Access.allow.name()).setTenant(testTenant),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forTenant.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }




    @Test
    @Order(5)
    public void testPermissionGroupForUser() {
        PermissionGroup forUserGroup = permissionGroupService.createPermissionGroup(new PermissionGroupCreate().setName("forUserGroup"), null);
        TestEntity forUser = testEntityService.createTestEntity(new TestEntityCreate().setName("forUser"), adminSecurityContext);
        PermissionGroupToBaseclass permissionGroupToBaseclass = permissionGroupToBaseclassService.createPermissionGroupToBaseclass(new PermissionGroupToBaseclassCreate().setPermissionGroup(forUserGroup).setBaseclass(forUser.getSecurity()), null);
        userToBaseclassService.createUserToBaseclass(new UserToBaseclassCreate().setUser(testUser).setBaseclass(forUserGroup).setValue(allOperation).setSimpleValue(IOperation.Access.allow.name()),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forUser.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }

    @Test
    @Order(6)
    public void testPermissionGroupForRole() {
        PermissionGroup forRoleGroup = permissionGroupService.createPermissionGroup(new PermissionGroupCreate().setName("forRoleGroup"), null);
        TestEntity forRole = testEntityService.createTestEntity(new TestEntityCreate().setName("forRole"), adminSecurityContext);
        PermissionGroupToBaseclass permissionGroupToBaseclass = permissionGroupToBaseclassService.createPermissionGroupToBaseclass(new PermissionGroupToBaseclassCreate().setPermissionGroup(forRoleGroup).setBaseclass(forRole.getSecurity()), null);
        roleToBaseclassService.createRoleToBaseclass(new RoleToBaseclassCreate().setRole(testRole).setBaseclass(forRoleGroup).setValue(allOperation).setSimpleValue(IOperation.Access.allow.name()),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forRole.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));
    }

    @Test
    @Order(7)
    public void testPermissionGroupForTenant() {
        PermissionGroup forTenantGroup = permissionGroupService.createPermissionGroup(new PermissionGroupCreate().setName("forTenantGroup"), null);
        TestEntity forTenant = testEntityService.createTestEntity(new TestEntityCreate().setName("forTenant"), adminSecurityContext);
        PermissionGroupToBaseclass permissionGroupToBaseclass = permissionGroupToBaseclassService.createPermissionGroupToBaseclass(new PermissionGroupToBaseclassCreate().setPermissionGroup(forTenantGroup).setBaseclass(forTenant.getSecurity()), null);
        tenantToBaseclassPermissionService.createTenantToBaseclassPermission(new TenantToBaseclassPermissionCreate().setBaseclass(forTenantGroup).setValue(allOperation).setSimpleValue(IOperation.Access.allow.name()).setTenant(testTenant),null);
        List<TestEntity> testEntities = testEntityService.listAllTestEntities(new TestEntityFilter(), testUserSecurityContext);
        Assertions.assertTrue(testEntities.stream().anyMatch(f->f.getId().equals(forTenant.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f-> othersInOtherTenantIds.contains(f.getId())));
        Assertions.assertTrue(testEntities.stream().noneMatch(f->othersInTenantIds.contains(f.getId())));

    }



}
