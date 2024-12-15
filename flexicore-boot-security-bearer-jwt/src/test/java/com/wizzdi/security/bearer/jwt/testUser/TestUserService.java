package com.wizzdi.security.bearer.jwt.testUser;

import com.flexicore.model.Baseclass;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityUserService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Extension
@Component
public class TestUserService implements Plugin {


    @Autowired
    private TestUserRepository repository;


    @Autowired
    private SecurityUserService securityUserService;
    @Autowired
    private SecurityContext adminSecurityContext;
    @Autowired
    private PasswordEncoder passwordEncoder;


    private static final Logger logger = LoggerFactory.getLogger(TestUserService.class);


    public TestUser createTestUser(TestUserCreate testUserCreate,
                                                             SecurityContext securityContext) {
        TestUser testUser = createTestUserNoMerge(testUserCreate,
                securityContext);
        repository.merge(testUser);
        return testUser;

    }

    public TestUser createTestUserNoMerge(TestUserCreate testUserCreate,
                                                                    SecurityContext securityContext) {
        TestUser testUser = new TestUser();
        testUser.setId(UUID.randomUUID().toString());
        updateTestUserNoMerge(testUser, testUserCreate);
        return testUser;
    }

    public boolean updateTestUserNoMerge(TestUser testUser, TestUserCreate testUserCreate) {
        boolean update = securityUserService.updateSecurityUserNoMerge(testUserCreate, testUser);

        if (testUserCreate.getPassword() != null && !passwordEncoder.matches(testUserCreate.getPassword(),testUser.getPassword())) {
            testUser.setPassword(passwordEncoder.encode(testUserCreate.getPassword()));
        }
        if (testUserCreate.getUsername() != null && !testUserCreate.getUsername().equals(testUser.getUsername())) {
            testUser.setUsername(testUserCreate.getUsername());
        }
        return update;
    }


    public List<TestUser> listAllTestUsers(TestUserFilter testUserFiltering,
                                                                     SecurityContext securityContext) {
        return repository.listAllTestUsers(testUserFiltering, securityContext);
    }



    public PaginationResponse<TestUser> getAllTestUsers(TestUserFilter testUserFiltering, SecurityContext securityContext) {
        List<TestUser> list = listAllTestUsers(testUserFiltering, securityContext);
        long count = repository.countAllTestUsers(testUserFiltering, securityContext);
        return new PaginationResponse<>(list, testUserFiltering, count);
    }

    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
        return repository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, securityContext);
    }


    public <T> T findByIdOrNull(Class<T> type, String id) {
        return repository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        repository.merge(base);
    }

    @Bean
    public TestUser defaultTestUser(){
        return createTestUser(new TestUserCreate().setUsername("admin").setPassword("admin").setName("admin"),adminSecurityContext);
    }
}
