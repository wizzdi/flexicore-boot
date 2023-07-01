package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.TenantToBaseClassPremission;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionCreate;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionFilter;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.TenantToBaseclassPermissionService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/tenantToBaseclassPermission")
@Extension
public class TenantToBaseclassPermissionController implements Plugin {

    @Autowired
    private TenantToBaseclassPermissionService tenantToBaseclassPermissionService;

    @IOperation(Name = "creates tenant to baseclass", Description = "creates tenant to baseclass")
    @PostMapping("/create")
    public TenantToBaseClassPremission create(@RequestBody @Validated(Create.class) TenantToBaseclassPermissionCreate tenantToBaseclassPermissionCreate, @RequestAttribute SecurityContextBase securityContext) {

        return tenantToBaseclassPermissionService.createTenantToBaseclassPermission(tenantToBaseclassPermissionCreate, securityContext);
    }

    @IOperation(Name = "returns tenant to baseclass", Description = "returns tenant to baseclass")
    @PostMapping("/getAll")
    public PaginationResponse<TenantToBaseClassPremission> getAll(@RequestBody @Valid TenantToBaseclassPermissionFilter tenantToBaseclassPermissionFilter, @RequestAttribute SecurityContextBase securityContext) {

        return tenantToBaseclassPermissionService.getAllTenantToBaseclassPermissions(tenantToBaseclassPermissionFilter, securityContext);
    }

    @IOperation(Name = "update tenant to baseclass", Description = "update tenant to baseclass")
    @PutMapping("/update")
    public TenantToBaseClassPremission update(@RequestBody @Validated(Update.class) TenantToBaseclassPermissionUpdate tenantToBaseclassPermissionUpdate, @RequestAttribute SecurityContextBase securityContext) {

        return tenantToBaseclassPermissionService.updateTenantToBaseclassPermission(tenantToBaseclassPermissionUpdate, securityContext);
    }
}
