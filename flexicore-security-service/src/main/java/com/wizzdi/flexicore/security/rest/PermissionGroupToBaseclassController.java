package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.PermissionGroupToBaseclassService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/permissionGroupToBaseclass")
@Extension
public class PermissionGroupToBaseclassController implements Plugin {

    @Autowired
    private PermissionGroupToBaseclassService permissionGroupToBaseclassService;

    @IOperation(Name = "creates PermissionGroupToBaseclass", Description = "creates PermissionGroupToBaseclass")
    @PostMapping("/create")
    public PermissionGroupToBaseclass create(@RequestBody @Validated(Create.class) PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupToBaseclassService.createPermissionGroupToBaseclass(permissionGroupToBaseclassCreate, securityContext);
    }

    @IOperation(Name = "returns PermissionGroupToBaseclass", Description = "returns PermissionGroupToBaseclass")
    @PostMapping("/getAll")
    public PaginationResponse<PermissionGroupToBaseclass> getAll(@RequestBody @Valid PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupToBaseclassService.getAllPermissionGroupToBaseclass(permissionGroupToBaseclassFilter, securityContext);
    }

    @IOperation(Name = "updates PermissionGroupToBaseclass", Description = "updates PermissionGroupToBaseclass")
    @PutMapping("/update")
    public PermissionGroupToBaseclass update(@RequestBody @Validated(Update.class) PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupToBaseclassService.updatePermissionGroupToBaseclass(permissionGroupToBaseclassUpdate, securityContext);
    }
}
