package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityTenant;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;
import com.wizzdi.flexicore.security.request.SecurityTenantFilter;
import com.wizzdi.flexicore.security.request.SecurityTenantUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityTenantService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/securityTenant")
@Extension
public class SecurityTenantController implements Plugin {

    @Autowired
    private SecurityTenantService tenantService;

    @IOperation(Name = "creates security tenant", Description = "creates security tenant")
    @PostMapping("/create")
    public SecurityTenant create(@RequestBody @Validated(Create.class) SecurityTenantCreate tenantCreate, @RequestAttribute SecurityContextBase securityContext) {

        return tenantService.createTenant(tenantCreate, securityContext);
    }

    @IOperation(Name = "returns security tenant", Description = "returns security tenant")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityTenant> getAll(@RequestBody @Valid SecurityTenantFilter tenantFilter, @RequestAttribute SecurityContextBase securityContext) {

        return tenantService.getAllTenants(tenantFilter, securityContext);
    }

    @IOperation(Name = "updates security tenant", Description = "updates security tenant")
    @PutMapping("/update")
    public SecurityTenant update(@RequestBody @Validated(Update.class) SecurityTenantUpdate tenantUpdate, @RequestAttribute SecurityContextBase securityContext) {

        return tenantService.updateTenant(tenantUpdate, securityContext);
    }
}
