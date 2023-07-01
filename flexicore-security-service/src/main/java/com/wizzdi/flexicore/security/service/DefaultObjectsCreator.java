package com.wizzdi.flexicore.security.service;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.model.Role;
import com.flexicore.model.RoleToUser;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.flexicore.model.TenantToUser;
import com.wizzdi.flexicore.security.interfaces.DefaultRoleProvider;
import com.wizzdi.flexicore.security.interfaces.DefaultRoleToUserProvider;
import com.wizzdi.flexicore.security.interfaces.DefaultTenantProvider;
import com.wizzdi.flexicore.security.interfaces.DefaultTenantToUserProvider;
import com.wizzdi.flexicore.security.interfaces.DefaultUserProvider;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.request.RoleToUserCreate;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;
import com.wizzdi.flexicore.security.response.Clazzes;
import com.wizzdi.flexicore.security.response.DefaultSecurityEntities;
import com.wizzdi.flexicore.security.response.TenantAndUserInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class DefaultObjectsCreator {
    private static final Logger logger= LoggerFactory.getLogger(DefaultObjectsCreator.class);

    private static final String DEFAULT_TENANT_ID = "jgV8M9d0Qd6owkPPFrbWIQ";
    private static final String TENANT_TO_USER_ID = "Xk5siBx+TyWv+G6V+XuSdw";
    private static final String SUPER_ADMIN_ROLE_ID = "HzFnw-nVR0Olq6WBvwKcQg";
    private static final String SUPER_ADMIN_TO_ADMIN_ID = "EbVFgr+YS3ezYUblzceVGA";
    @Autowired
    private SecurityTenantService tenantService;

    @Autowired
    private TenantToUserService tenantToUserService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleToUserService roleToUserService;
    @Autowired
    private SecurityUserService userService;
    @Autowired
    @Qualifier("systemAdminId")
    private String systemAdminId;


    @ConditionalOnMissingBean
    @Bean
    public DefaultTenantProvider<SecurityTenant> defaultTenantProvider() {
        return securityTenantCreate -> tenantService.createTenant(securityTenantCreate, null);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultUserProvider<SecurityUser> defaultSecurityUserProvider() {
        return securityUserCreate -> userService.createSecurityUser(securityUserCreate, null);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultTenantToUserProvider<TenantToUser> defaultTenantToUserProvider() {
        return tenantToUserCreate -> tenantToUserService.createTenantToUser(tenantToUserCreate, null);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultRoleProvider<Role> defaultRoleProvider() {
        return roleCreate -> roleService.createRole(roleCreate, null);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultRoleToUserProvider<RoleToUser> defaultRoleToUserProvider() {
        return roleToUserCreate -> roleToUserService.createRoleToUser(roleToUserCreate, null);
    }


    @Bean
    @ConditionalOnMissingBean
    public TenantAndUserInit tenantAndUserInit(DefaultTenantProvider defaultTenantProvider, DefaultUserProvider defaultUserProvider, DefaultTenantToUserProvider defaultTenantToUserProvider) {
        SecurityTenantCreate tenantCreate = new SecurityTenantCreate()
                .setIdForCreate(DEFAULT_TENANT_ID)
                .setName("Default SecurityTenant")
                .setDescription("Default SecurityTenant");
        SecurityTenant defaultTenant = tenantService.findByIdOrNull(SecurityTenant.class, DEFAULT_TENANT_ID);
        if (defaultTenant == null) {
            logger.debug("Creating Default SecurityTenant");
            defaultTenant = defaultTenantProvider.createDefaultTenant(tenantCreate);
        }
        if (defaultTenant.getTenant() == null) {
            defaultTenant.setTenant(defaultTenant);
            defaultTenant = tenantService.merge(defaultTenant);
        }

        SecurityUserCreate userCreate = new SecurityUserCreate()
                .setTenant(defaultTenant)
                .setName("Admin")
                .setIdForCreate(systemAdminId);
        SecurityUser admin = userService.findByIdOrNull(SecurityUser.class, systemAdminId);
        if (admin == null) {
            logger.debug("Creating Admin SecurityUser");
            admin = defaultUserProvider.createSecurityUser(userCreate);
        }

        if (admin.getCreator() == null) {
            admin.setCreator(admin);
            admin = userService.merge(admin);
        }


        if (defaultTenant.getCreator() == null) {
            defaultTenant.setCreator(admin);
            defaultTenant = tenantService.merge(defaultTenant);
        }

        TenantToUserCreate tenantToUserCreate = new TenantToUserCreate()
                .setDefaultTenant(true)
                .setSecurityUser(admin)
                .setTenant(defaultTenant)
                .setIdForCreate(TENANT_TO_USER_ID);
        TenantToUser tenantToUser = tenantToUserService.findByIdOrNull(TenantToUser.class, TENANT_TO_USER_ID);
        if (tenantToUser == null) {
            logger.debug("Creating SecurityTenant To SecurityUser link");
            tenantToUser = defaultTenantToUserProvider.createTenantToUser(tenantToUserCreate);
        }

        if (tenantToUser.getCreator() == null) {
            tenantToUser.setCreator(admin);
            defaultTenant = tenantService.merge(defaultTenant);
        }


        return new TenantAndUserInit(admin, defaultTenant,tenantToUser);
    }


    /**
     * creates all defaults instances, these are defined by the {@link AnnotatedClazz}
     */
    @ConditionalOnMissingBean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    public DefaultSecurityEntities createDefaultObjects(Clazzes clazzes, TenantAndUserInit tenantAndUserInit, DefaultRoleProvider defaultRoleProvider, DefaultRoleToUserProvider defaultRoleToUserProvider) {
        SecurityTenant defaultTenant = tenantAndUserInit.getDefaultTenant();
        SecurityUser admin = tenantAndUserInit.getAdmin();
        TenantToUser tenantToUser=tenantAndUserInit.getTenantToUser();

        RoleCreate roleCreate = new RoleCreate()
                .setTenant(defaultTenant)
                .setName("Super Administrators")
                .setDescription("Role for Super Administrators of the system")
                .setIdForCreate(SUPER_ADMIN_ROLE_ID);
        Role superAdminRole = roleService.findByIdOrNull(Role.class, SUPER_ADMIN_ROLE_ID);
        if (superAdminRole == null) {
            logger.debug("Creating Super Admin role");
            superAdminRole = defaultRoleProvider.createRole(roleCreate);
            superAdminRole.setTenant(defaultTenant);
            superAdminRole.setCreator(admin);
            superAdminRole = roleToUserService.merge(superAdminRole);
        }
        if(superAdminRole.getCreator()==null){
            superAdminRole.setCreator(admin);
            superAdminRole=roleService.merge(superAdminRole);
        }
        RoleToUserCreate roleToUserCreate = new RoleToUserCreate()
                .setRole(superAdminRole)
                .setSecurityUser(admin)
                .setTenant(defaultTenant)
                .setIdForCreate(SUPER_ADMIN_TO_ADMIN_ID);
        RoleToUser roleToUser = roleToUserService.findByIdOrNull(RoleToUser.class, SUPER_ADMIN_TO_ADMIN_ID);
        if (roleToUser == null) {
            logger.debug("Creating Role To SecurityUser Link");
            roleToUser = defaultRoleToUserProvider.createRoleToUser(roleToUserCreate);
        }
        if(roleToUser.getCreator()==null){
            roleToUser.setCreator(admin);
            roleToUser=roleToUserService.merge(roleToUser);
        }


        //roleToUserService.massMerge(toMerge);
        return new DefaultSecurityEntities(tenantAndUserInit.getAdmin(), tenantAndUserInit.getDefaultTenant(), superAdminRole, tenantToUser, roleToUser);

    }


}
