package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.TenantToBaseclass;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.TenantToBaseclassCreate;
import com.wizzdi.flexicore.security.request.TenantToBaseclassFilter;
import com.wizzdi.flexicore.security.request.TenantToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.TenantToBaseclassService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/tenantToBaseclass")
@Extension
public class TenantToBaseclassController implements Plugin {

    @Autowired
    private TenantToBaseclassService tenantToBaseclassService;

    @IOperation(Name = "creates tenant to baseclass", Description = "creates tenant to baseclass")
    @PostMapping("/create")
    public TenantToBaseclass create(@RequestBody @Validated(Create.class) TenantToBaseclassCreate tenantToBaseclassCreate, @RequestAttribute SecurityContext securityContext) {

        return tenantToBaseclassService.createTenantToBaseclass(tenantToBaseclassCreate, securityContext);
    }

    @IOperation(Name = "returns tenant to baseclass", Description = "returns tenant to baseclass")
    @PostMapping("/getAll")
    public PaginationResponse<TenantToBaseclass> getAll(@RequestBody @Valid TenantToBaseclassFilter tenantToBaseclassFilter, @RequestAttribute SecurityContext securityContext) {

        return tenantToBaseclassService.getAllTenantToBaseclasss(tenantToBaseclassFilter, securityContext);
    }

    @IOperation(Name = "update tenant to baseclass", Description = "update tenant to baseclass")
    @PutMapping("/update")
    public TenantToBaseclass update(@RequestBody @Validated(Update.class) TenantToBaseclassUpdate tenantToBaseclassUpdate, @RequestAttribute SecurityContext securityContext) {

        return tenantToBaseclassService.updateTenantToBaseclass(tenantToBaseclassUpdate, securityContext);
    }
}
