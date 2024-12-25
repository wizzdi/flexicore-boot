package com.wizzdi.flexicore.security.test.rest;

import com.flexicore.model.Clazz;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityLink;
import com.flexicore.model.SecurityLinkGroup;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.flexicore.model.UserToBaseclass;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.security.interfaces.SecurityContextProvider;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.request.RoleToUserCreate;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkGroupCreate;
import com.wizzdi.flexicore.security.request.SecurityLinkGroupFilter;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import com.wizzdi.flexicore.security.request.TenantToBaseclassCreate;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;
import com.wizzdi.flexicore.security.request.UserToBaseclassCreate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.response.SecurityLinkGroupContainer;
import com.wizzdi.flexicore.security.service.PermissionGroupService;
import com.wizzdi.flexicore.security.service.PermissionGroupToBaseclassService;
import com.wizzdi.flexicore.security.service.RoleService;
import com.wizzdi.flexicore.security.service.RoleToBaseclassService;
import com.wizzdi.flexicore.security.service.RoleToUserService;
import com.wizzdi.flexicore.security.service.SecurityLinkGroupService;
import com.wizzdi.flexicore.security.service.SecurityLinkService;
import com.wizzdi.flexicore.security.service.SecurityTenantService;
import com.wizzdi.flexicore.security.service.SecurityUserService;
import com.wizzdi.flexicore.security.service.TenantToBaseclassService;
import com.wizzdi.flexicore.security.service.TenantToUserService;
import com.wizzdi.flexicore.security.service.UserToBaseclassService;
import com.wizzdi.flexicore.security.test.app.App;
import com.wizzdi.flexicore.security.test.app.TestEntity;
import com.wizzdi.flexicore.security.test.app.TestEntityCreate;
import com.wizzdi.flexicore.security.test.app.TestEntityService;
import com.wizzdi.segmantix.model.Access;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour


public class SecurityLinkGroupControllerTest {
    private final static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15")

            .withDatabaseName("flexicore-test")
            .withUsername("flexicore")
            .withPassword("flexicore");

    static {
        postgresqlContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    private SecurityLink securityLink;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SecurityLinkService securityLinkService;
    @Autowired
    private SecurityLinkGroupService securityLinkGroupService;


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
    private SecurityContext adminSecurityContext;
    @Autowired
    private UserToBaseclassService userToBaseclassService;
    @Autowired
    private PermissionGroupToBaseclassService permissionGroupToBaseclassService;
    @Autowired
    private RoleToBaseclassService roleToBaseclassService;
    @Autowired
    private TenantToBaseclassService tenantToBaseclassService;
    @Autowired
    private PermissionGroupService permissionGroupService;


    private SecurityContext testUserSecurityContext;
    private Role testRole;
    private SecurityTenant testTenant;
    private SecurityUser testUser;

    private Set<String> othersInTenantIds = new HashSet<>();
    private Set<String> othersInOtherTenantIds = new HashSet<>();

    @BeforeAll
    public void init() {
        ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return !response.getStatusCode().is2xxSuccessful();
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                String responseBody = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
                throw new HttpClientErrorException(response.getStatusCode(), "%s:%s".formatted(response.getStatusText(), responseBody));
            }
        };
        restTemplate.getRestTemplate().setErrorHandler(errorHandler);
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("authenticationKey", "fake");
                    return execution.execute(request, body);
                }));

        testUser = securityUserService.createSecurityUser(new SecurityUserCreate().setName("testUser"), null);
        testTenant = securityTenantService.createTenant(new SecurityTenantCreate().setName("testTenant"), null);
        tenantToUserService.createTenantToUser(new TenantToUserCreate().setUser(testUser).setDefaultTenant(true).setTenant(testTenant), null);
        SecurityLinkGroup group = securityLinkGroupService.createSecurityLinkGroup(new SecurityLinkGroupCreate().setName("group"), adminSecurityContext);


        testUserSecurityContext = securityContextProvider.getSecurityContext(testUser);
        othersInOtherTenantIds = IntStream.range(0, 10).mapToObj(f -> testEntityService.createTestEntity(new TestEntityCreate().setName("othersInOtherTenant" + f), adminSecurityContext)).map(f -> f.getId()).collect(Collectors.toSet());
        List<TestEntity> list = IntStream.range(0, 10).mapToObj(f -> testEntityService.createTestEntity(new TestEntityCreate().setName("othersInTenant" + f), adminSecurityContext)).toList();
        for (TestEntity testEntity : list) {
            testEntity.setTenant(testTenant);
            testEntityService.merge(testEntity);
            UserToBaseclassCreate userToBaseclassCreate = new UserToBaseclassCreate()
                    .setUser(testUser)
                    .setAccess(Access.allow)
                    .setSecurityLinkGroup(group)
                    .setSecuredId(testEntity.getSecurityId())
                    .setClazz(Clazz.ofClass(TestEntity.class));
            userToBaseclassService.createUserToBaseclass(userToBaseclassCreate,adminSecurityContext);
        }
    }


    @Test
    @Order(1)
    public void testListAllSecurityLinkContainers() {

        SecurityLinkGroupFilter request = new SecurityLinkGroupFilter()
                .setSecurityLinkFilter(new SecurityLinkFilter().setRelevantUserIds(Set.of(testUser.getId())))
                .setPageSize(5)
                .setCurrentPage(0);
        ParameterizedTypeReference<PaginationResponse<SecurityLinkGroupContainer>> t = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<PaginationResponse<SecurityLinkGroupContainer>> securityLinkResponse = this.restTemplate.exchange("/securityLinkGroup/getAllContainers", HttpMethod.POST, new HttpEntity<>(request), t);
        Assertions.assertEquals(200, securityLinkResponse.getStatusCode().value());
        PaginationResponse<SecurityLinkGroupContainer> body = securityLinkResponse.getBody();
        Assertions.assertNotNull(body);
        List<SecurityLinkGroupContainer> securityLinks = body.getList();
        Assertions.assertNotEquals(0, securityLinks.size());
        Map<String,List<SecurityLinkGroupContainer>> countMap=securityLinks.stream().collect(Collectors.groupingBy(f->f.securityLinkGroup().getId()));
        for (Map.Entry<String, List<SecurityLinkGroupContainer>> entry : countMap.entrySet()) {
            Assertions.assertTrue(entry.getValue().size()<2);

        }


    }


}
