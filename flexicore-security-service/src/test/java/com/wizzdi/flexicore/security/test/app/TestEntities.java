package com.wizzdi.flexicore.security.test.app;

import com.flexicore.model.PermissionGroup;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.PermissionGroupCreate;
import com.wizzdi.flexicore.security.service.PermissionGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestEntities {

    @Autowired
    private PermissionGroupService permissionGroupService;
    @Autowired
    private SecurityContextBase adminSecurityContext;

    @Bean
    public PermissionGroup permissionGroup() {
        return permissionGroupService.createPermissionGroup(new PermissionGroupCreate().setExternalId("test").setName("test"), adminSecurityContext);
    }


}
