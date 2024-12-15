package com.wizzdi.flexicore.security.test.app;

import com.flexicore.model.PermissionGroup;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.security.request.PermissionGroupCreate;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.service.PermissionGroupService;
import com.wizzdi.flexicore.security.service.RoleService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestEntities {



    @Bean
    public PermissionGroup permissionGroup(PermissionGroupService permissionGroupService, SecurityContext adminSecurityContext) {
        return permissionGroupService.createPermissionGroup(new PermissionGroupCreate().setExternalId("test").setName("test"), adminSecurityContext);
    }

    @Bean
    @Qualifier("roleTest")
    public Role roleTest(RoleService roleService, SecurityContext adminSecurityContext) {
        return roleService.createRole(new RoleCreate().setTenant((SecurityTenant) adminSecurityContext.getTenantToCreateIn()).setName("test"), adminSecurityContext);
    }


}
