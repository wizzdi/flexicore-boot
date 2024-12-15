package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.TenantToUser;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;
import com.wizzdi.flexicore.security.request.TenantToUserFilter;
import com.wizzdi.flexicore.security.request.TenantToUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.TenantToUserService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/tenantToUser")
@Extension
public class TenantToUserController implements Plugin {

    @Autowired
    private TenantToUserService tenantToUserService;

    @IOperation(Name = "create tenant to user", Description = "creates tenant to user")
    @PostMapping("/create")
    public TenantToUser create(@RequestBody @Validated(Create.class) TenantToUserCreate tenantToUserCreate, @RequestAttribute SecurityContext securityContext) {

        return tenantToUserService.createTenantToUser(tenantToUserCreate, securityContext);
    }

    @IOperation(Name = "get all tenant to user", Description = "get all tenant to user")
    @PostMapping("/getAll")
    public PaginationResponse<TenantToUser> getAll(@RequestBody @Valid TenantToUserFilter tenantToUserFilter, @RequestAttribute SecurityContext securityContext) {

        return tenantToUserService.getAllTenantToUsers(tenantToUserFilter, securityContext);
    }

    @IOperation(Name = "updates tenant to user", Description = "updates tenant to user")
    @PutMapping("/update")
    public TenantToUser update(@RequestBody @Validated(Update.class) TenantToUserUpdate tenantToUserUpdate, @RequestAttribute SecurityContext securityContext) {

        return tenantToUserService.updateTenantToUser(tenantToUserUpdate, securityContext);
    }
}
